```
 ____  ____   ___  ____  _     _____     _____ ___ _
/ ___||  _ \ / _ \|  _ \| |   |_ _\ \   / / _ |_ _| |
\___ \| |_) | | | | | | | |    | | \ \ / | | | | || |
 ___) |  __/| |_| | |_| | |___ | |  \ V /| |_| | ||_|
|____/|_|    \___/|____/|_____|___|  \_/  \___|___(_)

```

[![actions-workflow-test][actions-workflow-test-badge]][actions-workflow-test]
[![license][license-badge]][license]

Create new bot with [@BotFather](https://t.me/BotFather) and fill your values in `docker-compose.yml`.  
You should replace `TELEGRAM_BOT_TOKEN` and `TELEGRAM_BOT_USERNAME` at least before starting.  
Start is simple as `docker-compose up --build -d`  

**Note:** Docker has very annoing bug https://github.com/moby/moby/issues/2259 that will cause exception on first start:  
```Unable to create file /var/log/spodlivoi.log java.io.IOException: Permission denied```   
If you encountered this, run once on your host: ```chown -R $(docker-compose exec spodlivoi id -u) $(docker inspect spodlivoibot_spodlivoilogs -f {{.Mountpoint}})```
Although, if you don't need persistent logs, you can simply ignore this.

Demo: [@spodlivoi_bot](https://t.me/spodlivoi_bot)  

<!-- badge links -->

[actions-workflow-test]: https://github.com/tsunamaru/spodlivoibot/actions?query=workflow%3ADeployment
[actions-workflow-test-badge]: https://img.shields.io/github/workflow/status/tsunamaru/spodlivoibot/Deployment?label=CI&style=for-the-badge&logo=github
[license]: LICENSE
[license-badge]: https://img.shields.io/github/license/tsunamaru/spodlivoibot?style=for-the-badge
