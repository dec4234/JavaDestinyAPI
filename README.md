<p align="center">
    <img src="https://user-images.githubusercontent.com/22875520/119843615-b6da3f80-bed5-11eb-8d3e-b2432a993454.png">
    <a href="https://discord.gg/dvZmP92d4h"><img src="https://discordapp.com/api/guilds/847480795232993280/widget.png?style=banner2"></a>
</p>

# JavaDestinyAPI 
This library is used as an interface to the API for the game Destiny 2, created by Bungie.

The API is a work in-progress, contributions are welcome. Please note that development is on-going and the latest versions may be prone to bugs. If you find any please create an issue to report it.
## Getting Started
This project can be currently accessed through the jitpack repository, which allows any github repo to be used as a dependency
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
And then this dependency:
```xml
<dependency>
    <groupId>com.github.dec4234</groupId>
    <artifactId>JavaDestinyAPI</artifactId>
    <version>master</version>
</dependency>
```
(You may need to replace "master" with the latest commit hash)

If you happen to need a JAR version, check out [releases](https://github.com/dec4234/JavaDestinyAPI/releases).

****

**Getting the API Set Up**

You need to get an API key from [bungie.net/developer](https://bungie.net/developer)

```java
DestinyAPI api = new DestinyAPI("YOUR API KEY HERE");
```
**IMPORTANT:** *DestinyAPI MUST be the first thing initialized before making any interactions with the API! This is because
all interactions with the API rely on the API Key set in DestinyAPI.*

**Getting the time played in hours, of the user named dec4234#9904**
```java
System.out.println(DestinyAPI.getUserWithName("dec4234#9904").getTimePlayed() / 60.0);
```

**Get the name of the founder of a clan**
```java
System.out.println(new Clan("Heavenly Mayhem").getFounder().getSupplementalDisplayName());
```

Check out the [wiki](https://github.com/dec4234/JavaDestinyAPI/wiki/Getting-Started) for more specific examples and information.

### An aside about APIException
As of 4/30/2024, all functions that interact with the API in any way (i.e. could make an HTTP request) throw APIException.
This is to allow for you, the user, to handle API errors in the way you see fit. This means you must use a try/catch or
add "throws" to your function signature.
<br>
This was done because the old way could create a lot of runtime exceptions which were hard to account for.
The most important error you may want to look at is [APIOfflineException](https://github.com/dec4234/JavaDestinyAPI/blob/master/src/main/java/net/dec4234/javadestinyapi/exceptions/APIOfflineException.java)
which indicates when Bungie has disabled the API for maintenence. This may be useful to tell your user's that the API is offline.

## How's it made?
There is both official and unofficial documentation for the API available on [destinydevs.github.io](http://destinydevs.github.io/BungieNetPlatform/docs/Endpoints) and on the [offical bungie api documentation](https://bungie-net.github.io/).

### TO-DO
- Managing inventory / Item transferring
- Collections / Triumphs
- Revamping the wiki