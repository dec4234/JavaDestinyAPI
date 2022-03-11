<p align="center">
    <img src="https://user-images.githubusercontent.com/22875520/119843615-b6da3f80-bed5-11eb-8d3e-b2432a993454.png">
</p>

[![Discord Banner 2](https://discordapp.com/api/guilds/847480795232993280/widget.png?style=banner2)](https://discord.gg/dvZmP92d4h)

# JavaDestinyAPI 
This is a java wrapper for the API provided by bungie to control things on Bungie.net and inside the game Destiny 2. It handles all of the neccessary HTTP requests async and parses the Json response for you.

The API is a work in-progress, contributions are welcome. Please note that development is on-going and the latest versions may be prone to bugs. If you find any please create an issue to report it.
## Getting Started
This project can be currently accessed through the jitpack repository, which allows any github repo to be used as a dependency
```
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
And then this dependency:
```
<dependency>
    <groupId>com.github.dec4234</groupId>
    <artifactId>JavaDestinyAPI</artifactId>
    <version>master</version>
</dependency>
```
(You may need to replace "master" with the latest commit hash)

If you happen to need a JAR version, check out [releases](https://github.com/dec4234/JavaDestinyAPI/releases).

Check out the [wiki](https://github.com/dec4234/JavaDestinyAPI/wiki/Getting-Started) for more specific examples and information.

## How's it made?
There is both official and unofficial documentation for the API available on [destinydevs.github.io](http://destinydevs.github.io/BungieNetPlatform/docs/Endpoints) and on the [offical bungie api documentation](https://bungie-net.github.io/).

### TO-DO
- Managing inventory / Item transferring
- Collections / Triumphs
- Comprehensive Wiki / Youtube tutorials

### Contributing
All development since 3/10/2022 on new features occurs within the [dev](https://github.com/dec4234/JavaDestinyAPI/tree/dev) branch. Clone that and submit a pull request with your changes.