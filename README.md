Simple social app server
========

This project contains a basic server for social media app.


How to use the server:
  1) set the following fields in App class:
  - HOSTNAME
  - PORT
  2) run App.main()
  
How to test:
-------
  edit js/scripts.js file and set {values} in line:
  
    var socket = new WebSocket("ws://{HOSTNAME}:{PORT}/wall/" + name);
    
  open index.html file in browser
  
  add to url:
  
      ?username=your name
      
      for example: directory/index.html?username=test
  
 

Features
--------

- posting messages with length up to 140 characters
- receiving own posts
- following/unfollowing other users
- receiving list of active users
- receiving followed users' posts

Installation
------------

Install project:

    mvn install
    
Run project:
go into target directory and:
  java -jar {artifact-name}.jar



API
========


Connect to API
------------
```
ws://{hostname}:{port}/wall/{username}
```


List of events (pair: "name - code"):
```
Client sends to server
  Follow - FO
  Unfollow - UF
  New post - NP 
Server sends to clients
  Active users - AU
  Posts - PO
  Own posts - OP
```

Sending requests
------------

To follow a user:
```
  {
    "eventCode" : "FO",
    "user" : "current username",
    "followed" : "username to follow"
  }
```

To unfollow a user:
```
  {
    "eventCode" : "UF",
    "user" : "current username",
    "followed" : "username to follow"
  }
```

To post:
```
  {
    "eventCode" : "NP",
    "content" : "content of the post (will be trimmed to 140 characters if is longer)",
    "timestamp" : "current timestamp",
    "user" : "current username"
  }
```

Receiving requests
------------

Active users (receiving only usernames):
```
  { 
    "eventCode" : "AU",
    [{"username1", "username2", ... , "usernamen"}]
  }
```

Posts (only followed users):
```
  { 
    "eventCode" : "PO",
    [{
      "content" : "content of the post",
      "timestamp" : "post's timestamp",
      "user" : "author's username"
    }]
  }
```


Own posts (only users' posts):
```
  { 
    "eventCode" : "OP",
    [{
      "content" : "content of the post",
      "timestamp" : "post's timestamp",
      "user" : "author's username"
    }]
  }
```
