```
░██████╗██████╗░░█████╗░██████╗░██╗░░░░░██╗██╗░░░██╗░█████╗░██╗██╗
██╔════╝██╔══██╗██╔══██╗██╔══██╗██║░░░░░██║██║░░░██║██╔══██╗██║██║
╚█████╗░██████╔╝██║░░██║██║░░██║██║░░░░░██║╚██╗░██╔╝██║░░██║██║██║
░╚═══██╗██╔═══╝░██║░░██║██║░░██║██║░░░░░██║░╚████╔╝░██║░░██║██║╚═╝
██████╔╝██║░░░░░╚█████╔╝██████╔╝███████╗██║░░╚██╔╝░░╚█████╔╝██║██╗
╚═════╝░╚═╝░░░░░░╚════╝░╚═════╝░╚══════╝╚═╝░░░╚═╝░░░░╚════╝░╚═╝╚═╝
```

[![actions-workflow-test][actions-workflow-test-badge]][actions-workflow-test]
[![license][license-badge]][license]

#### Bot commands (stanalone)
- `/roll` - rolls your dick (and/or vagina) and anus size (configurable by `/setting`)
- `/fight` - random humilation pic
- `/webm` - random .webm posted on 2ch.hk/b
- `/bred` - random thread created on 2ch.hk/b
- `/olds`, `/baby`, `/shizik`, `/kolchan`, `/dota` - random copypaste
- `/setting` - yes
- `/top` - yeeeeeeeees
- `/test` - healthcheck (hidden)
- `/videostats` - how many videos processing right now (hidden)
#### Bot commands (inline)
- All copypaste set + a special one
- `fight` or `боевая` - give you 10 random humilation pics
#### Passive bot features
- Converts every posted in chat .webm to .mp4
- Error reporting in admin chat (if set)
- Copypaste DDOS by invoking `/ddos_activate <user-id>` in admin chat
- Silent delete user invoked command, if it points to another post
- Every 12:00 PM (UTC+3) bot decides, who is posted most cringy thing in every chat (shitpost of the day)
#### Upcoming features
- Faggot of the day
- RPG-like events

### Contributing
I don't think you ever want to.

### Code of Conduct
Just go fuck yourself.

### Screenshots
Better go see it with your own eyes.

### Installation
Create new bot with [@BotFather](https://t.me/BotFather) and fill your values in `docker-compose.yml`.  
You should replace `TELEGRAM_BOT_TOKEN` and `TELEGRAM_BOT_USERNAME` at least before starting.  
If you want to trigger admin commands and get error reporting, set chat ID in `TELEGRAM_BOT_ADMIN-CHAT-ID`.  
Start is simple as `docker-compose --compatibility up --build -d`  

Demo: [@spodlivoi_bot](https://t.me/spodlivoi_bot)  

<!-- badge links -->

[actions-workflow-test]: https://github.com/tsunamaru/spodlivoibot/actions?query=workflow%3ADeployment
[actions-workflow-test-badge]: https://img.shields.io/github/workflow/status/tsunamaru/spodlivoibot/Deployment?label=CI&style=for-the-badge&logo=github
[license]: LICENSE
[license-badge]: https://img.shields.io/github/license/tsunamaru/spodlivoibot?style=for-the-badge
