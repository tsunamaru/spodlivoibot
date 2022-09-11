# k8s workflow
## Pre-requisites
- [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
- [helm](https://helm.sh/docs/intro/install/)
- [flux/v2](https://fluxcd.io/flux/get-started/)
- [kubeseal](https://sealed-secrets.netlify.app/)
- [terraform](https://www.terraform.io/downloads.html) (optional)

## First time setup
### Encrypted secrets
NB: assuming you have flux installed and running
```
$ flux create source helm sealed-secrets \
--interval=1h \
--url=https://bitnami-labs.github.io/sealed-secrets
$ flux create helmrelease sealed-secrets \
--interval=1h \
--release-name=sealed-secrets-controller \
--target-namespace=flux-system \
--source=HelmRepository/sealed-secrets \
--chart=sealed-secrets \
--chart-version=">=1.15.0-0" \
--crds=CreateReplace
$ kubeseal --fetch-cert \
--controller-name=sealed-secrets-controller \
--controller-namespace=flux-system \
> pub-sealed-secrets.pem
$ kubectl apply -f ns.yaml
$ kubectl create -n spodlivoi secret generic bot-secrets \
--from-literal=token=<...> \
--from-literal=username=<...> \
--from-literal=adm_chat_id=<...> \
--dry-run=client -o yaml > secrets.yaml
$ kubeseal --format=yaml --cert=pub-sealed-secrets.pem \
< secrets.yaml > encrypted_secrets.yaml
$ kubectl apply -f encrypted_secrets.yaml
$ rm -f secrets.yaml
```

### EFS storage class (postgres)
You can skip this step and use default storage class instead.
#### Terraform version
```
resource "kubernetes_storage_class" "efs_postgres_sc" {
  metadata {
    name = "efs-postgres-sc"
  }

  storage_provisioner = "efs.csi.aws.com"

  parameters = {
    basePath         = "/postgres"
    directoryPerms   = "700"
    fileSystemId     = <...>
    provisioningMode = "efs-ap"
    gid              = "70"
    uid              = "70"
  }
}
```
#### Plain kubectl version
```
allowVolumeExpansion: true
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: efs-postgres-sc
parameters:
  basePath: /postgres
  directoryPerms: "700"
  fileSystemId: <...>
  gid: "70"
  provisioningMode: efs-ap
  uid: "70"
provisioner: efs.csi.aws.com
reclaimPolicy: Delete
volumeBindingMode: Immediate
```

### Deploy
```
$ kubectl apply -f deployment.yaml
```

### Additional RBAC+sa for github-actions
```
$ kubectl apply -f _ns_ci.yaml
$ kubectl describe secret -n spodlivoi ns-si-sa-token-<...> -oyaml
```
Save token to kubeconfig and upload it to repository secrets. See workflows for more details.