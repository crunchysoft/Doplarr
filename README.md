<p align="center">
  <img src="https://raw.githubusercontent.com/kiranshila/Doplarr/main/logos/logo_cropped.png" width="200">
</p>

</p>
<p align="center">
<img alt="GitHub Workflow Status" src="https://img.shields.io/github/workflow/status/kiranshila/doplarr/Main?style=for-the-badge">
<img alt="Lines of code" src="https://img.shields.io/tokei/lines/github/kiranshila/doplarr?style=for-the-badge">
<img alt="Code" src="https://img.shields.io/badge/code-data-blueviolet?style=for-the-badge">
</p>

> A _Better_ Sonarr/Radarr Request Bot for Discord

## Why does this exist

- Uses modern Discord slash commands and components, which provides a clean, performant UI on desktop and mobile
- Simple codebase, <1k lines of code which _should_ make it easy to maintain
- Simple configuration, no need to have a whole web frontend
- Powered by Clojure and [Discljord](https://github.com/IGJoshua/discljord), a markedly good language 😛

### Caveats

I wanted a clean app for the sole purpose of requesting movies/TV shows.
I personally didn't need Siri, Overseerr, or Ombi integration - so those are missing in this bot (for now).
There is only a boolean permission (role gated) for who has access to the bot, nothing fancy.

If any of these don't suit your fancy, check out [Requestrr](https://github.com/darkalfx/requestrr)

Please consider this software "beta enough" as some of the error handling is pretty rough around the edges - however I've been using this for a bit now on my own server without any issues, but of course your mileage may vary.

### Screenshots

<img src="https://raw.githubusercontent.com/kiranshila/Doplarr/main/screenshots/Request.png" width="400">
<img src="https://raw.githubusercontent.com/kiranshila/Doplarr/main/screenshots/Selection.png" width="400">
<img src="https://raw.githubusercontent.com/kiranshila/Doplarr/main/screenshots/button.png" width="400">

## Setup

### Java

If you are running without Docker, you need to have at least Java 11 installed, such as [adoptopenjdk](https://adoptopenjdk.net/)

### Discord

1. Create a new [Application](https://discord.com/developers/applications) in Discord
2. Go to the Bot tab and add a new bot
3. Copy out the token
4. Go to OAuth2 and under "OAuth2 URL Generator", enable `applications.commands` and `bot`
5. Copy the resulting URL and use as the invite link to your server

In the server for which you will use the bot, you need to create a new role for
your users. Then, grab that role id.

To do this:

1. Enable Developer Mode (User Settings -> Advanced -> Developer Mode)
2. Under your server settings, go to Roles, find the role and "Copy ID"

### Sonarr/Radarr

1. Copy out your API keys from Settings -> General

## Running with Docker

Simply run with

```bash
docker run \
-e SONARR_URL='http://localhost:8989' \
-e RADARR_URL='http://localhost:7878' \
-e SONARR_API='sonarr_api' \
-e RADARR_API='radarr_api' \
-e BOT_TOKEN='bot_token' \
-e ROLE_ID='role_id' \
--name doplarr ghcr.io/kiranshila/doplarr:latest

```

## Building and Running Locally

You need the Clojure CLI tools to build

1. Clone directory
2. `clj -T:build uberjar`

To skip the build, just download `Doplarr.jar` and `config.edn` from the releases

## Configuring

1. Fill out `config.edn` with the requisite things

### Optional Settings

Also, I'm limiting the size of the results in the drop down to 10, this can be
set with `:max-results` in the config file of `MAX_RESULTS` as an environment variable.

### Setting up on Windows

1. Make a folder called Doplarr that contains the jar, the config file, and the
   following batch file (something like `run.bat`)

```batchfile
@ECHO OFF
start java -jar Doplarr.jar -Dconfig=config.edn
```

### Setting up on Linux (as a systemd service)

1. Create the file /etc/systemd/system/doplarr.service with the following

```ini
[Unit]
Description=Doplarr Daemon
After=syslog.target network.target
[Service]
User=root
Group=root
Type=simple
WorkingDirectory=/opt/Doplarr
ExecStart=/usr/bin/java -jar target/Doplarr.jar -Dconfig=config.edn
TimeoutStopSec=20
KillMode=process
Restart=always
[Install]
WantedBy=multi-user.target
```

2. Customize the user, group, and working directory to the location of the jar

Then, as root

3. `systemctl -q daemon-reload`
4. `systemctl enable --now -q doplarr`
