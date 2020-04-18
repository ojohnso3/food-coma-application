<!DOCTYPE html>
<#--  <html lang="en">  -->
    <head>
        <meta charset="utf-8">
        <title>Log-in Portal</title>

        <link rel="icon" href="./Q_favicon.png">
        <link rel="stylesheet" type="text/css" href="main.css">
        <link href='https://fonts.googleapis.com/css?family=Bebas Neue: 100,400,700' rel='stylesheet'>
        <link href='https://fonts.googleapis.com/css?family=Open Sans: 100,400,700' rel='stylesheet'>
    </head>
    <body>

        <h1>Log in to Account!</h1>

        <a href="main.ftl"> <img src="foodCOMA.png" id="foodCOMA"> </a>
        <h2> LOG-IN </h2>
        <div class="buttonBox">
          <form action="query.ftl"> <#--  userqueue  -->
            <div class="formItem">
              Username: <input type="text" name="username", id="name">
            </div>
            <div class="formItem">
              Password: <input type="text" name="password", id="pass">
            </div>
            <input type="button" value="Submit", onclick="createUser()">
          </form>
        </div>
    </body>

    <script src="https://www.gstatic.com/firebasejs/7.7.0/firebase.js"></script>
    <script src="newQ.js"> </script>
    <script src="garage.js"> </script>

</html>