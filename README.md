# JavaDestinyAPI
This is a java wrapper for the API provided by bungie to control things on Bungie.net and inside the game Destiny 2. It handles all of the neccessary HTTP requests async and parses the Json response for you.

The API is far from complete, and currently only handles the basics of what you might need. You can get users and clans by their names or ids and get details about them. You can also control your clan such as kicking, banning and inviting players, as long as you have set up OAuth.

## Getting Started
This project can be currently accesed through the jitpack repository, which allows any github repo to be used as a dependency
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

Check out the [wiki](https://github.com/dec4234/JavaDestinyAPI/wiki/Getting-Started) for more specific examples and information.

## How's it made?
There is both offical and unoffical documentation for the API available on [destinydevs.github.io](http://destinydevs.github.io/BungieNetPlatform/docs/Endpoints) and on the [offical bungie api documentation](https://bungie-net.github.io/).
