# JavaDestinyAPI
This is a java wrapper for the api provided by bungie to control things on Bungie.net and inside the game Destiny 2. It handles all of the neccessary HTTP requests async and parses the Json response for you.

The API is far from complete, and currently only handles the basics of what you might need. You can get users and clans by their names or ids and get details about them. You can also control your clan such as kicking, banning and inviting players, as long as you have set up OAuth.

# Getting Started
To begin, use this dependency inside of your Maven pom.xml
```
<groupId>org.dec4234</groupId>
<artifactId>JavaDestinyAPI</artifactId>
<version>1.0</version>
```

Now you can start utilizing features of the api. The core class is called DestinyAPI, this is where you set your api key, and optionally: the client id, secret, and oauth code.
```java
DestinyAPI api = new DestinyAPI().setApiKey("YOUR_API_KEY_HERE").setClientID("CLIENT_ID").setClientSecret("CLIENT_SECRET").setOauthCode("OAUTH_CODE");
```
DestinyAPI __has__ to be initialized prior to any of the features inside the API that make requests to Bungie.net. The Client id, client secret and oauth code only need to be used whenever oauth is in play, so to start you don't need to bother with them.
