# k8s workflow
## Pre-requisites
- [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
- [helm](https://helm.sh/docs/intro/install/)
- [flux/v2](https://fluxcd.io/flux/get-started/)
- [kubeseal](https://sealed-secrets.netlify.app/)

## First time setup
Encrypted secrets (assuming you have flux installed and running):
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