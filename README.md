<p align="center">
    <img src="https://user-images.githubusercontent.com/22875520/119843615-b6da3f80-bed5-11eb-8d3e-b2432a993454.png">
</p>

[![Discord Banner 2](https://discordapp.com/api/guilds/847480795232993280/widget.png?style=banner2)](https://discord.gg/dvZmP92d4h)

# JavaDestinyAPI 
This is a java wrapper for the API provided by bungie to control things on Bungie.net and inside the game Destiny 2. It handles all of the neccessary HTTP requests async and parses the Json response for you.

The API is far from complete, and currently only handles the basics of what you might need. You can get users and clans by their names or ids and get details about them. You can also control your clan such as kicking, banning and inviting players, as long as you have set up OAuth.

## Getting Started
This is a java wrapper for the API provided by bungie to control things on the Bungie.net platform.
The API is currently capable of handling most things, such as retrieving stats, managing your clan and making simple requests. 

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

If you happen to need a JAR version, check out [releases](https://github.com/dec4234/JavaDestinyAPI/releases).

Check out the [wiki](https://github.com/dec4234/JavaDestinyAPI/wiki/Getting-Started) for more specific examples and information.

## How's it made?
There is both official and unofficial documentation for the API available on [destinydevs.github.io](http://destinydevs.github.io/BungieNetPlatform/docs/Endpoints) and on the [offical bungie api documentation](https://bungie-net.github.io/).

### TO-DO
- Revamping exisiting project structure
- Managing inventory / Item transferring
- Collections / Triumphs
- Comprehensive Wiki / Youtube tutorials

### Future Plans
I have some long-term goals with this API. Firstly, I'd like to complete the wrapping of most of Bungie-net API into the JavaDestinyAPI. Next I'd like to make a 2.0 version of this API completely built from the ground-up using the lessons and principles learned and established within the development period of the 1.0 version.
