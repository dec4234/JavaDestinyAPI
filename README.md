# JavaDestinyAPI
This is a java wrapper for the API provided by bungie to control things on Bungie.net and inside the game Destiny 2. It handles all of the neccessary HTTP requests async and parses the Json response for you.

The API is far from complete, and currently only handles the basics of what you might need. You can get users and clans by their names or ids and get details about them. You can also control your clan such as kicking, banning and inviting players, as long as you have set up OAuth.

# Getting Started
To begin, use this dependency inside of your Maven pom.xml
```
<groupId>org.dec4234</groupId>
<artifactId>JavaDestinyAPI</artifactId>
<version>1.0</version>
```

Check out the [wiki](https://github.com/dec4234/JavaDestinyAPI/wiki/Getting-Started) for more specific examples and information.
