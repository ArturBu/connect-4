RESTFul Connect-4:

The game rules are described well here: https://en.wikipedia.org/wiki/Connect_Four

To run this app you need:

1. Web container like Tomcat, Jetty etc.
2. Redis database. Please configure the ip and port of redis in "game.properties" file.

Only authenticated users can play the game. There are two users already added (later it can be extended to use real database instead with no limit users):
1. Login: Joao 
   Password: password 
   Base64: Sm9hbzpwYXNzd29yZA==
2. Login: Artur
   Password: password
   Base64: QXJ0dXI6cGFzc3dvcmQ=
   
 
 Example of gameplay:
 
1. Start the game:
 
 Execute GET on (where "connect-4" u may need to use artifact name - "connect-4-0.0.1-SNAPSHOT"):
 http://localhost:8080/connect-4/game/start?playerOneName=Johny&playerTwoName=Bill 
 Headers: 
 - Authorization: Basic Sm9hbzpwYXNzd29yZA==
 
2. In the answer you will receive:
  - {"message":"Waiting for player: Jhony to move.","gameStateEnum":"STARTED"}
  - x-auth-token: 5c04de32-3e83-4d5e-a9ac-5b3850e02780
  - 
  After you received x-auth-token you should use it in HEADER instead of "Authorization: Basic Sm9hbzpwYXNzd29yZA=="

3. Now is time for the first move:

 Execute GET (where Bill is playr name and where 0 is the column number to which you wanna put coin):
 http://localhost:8080/connect-4/game/move/Bill/0
 Headers: 
 - x-auth-token: 5c04de32-3e83-4d5e-a9ac-5b3850e02780
 
 The first column has index 0 !
