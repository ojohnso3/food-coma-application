<#assign content>

<h1>Log in Below!</h1>

<h2> LOG-IN </h2>
<div class="buttonBox">
    <input id="user" class="u-full-width"
        type="text"
        placeholder="username"
        value=""></input>
    <br>    
    <input id="pass" class="u-full-width"
        type="text"
        placeholder="password"
        value=""></input>
    <br>
    <button id="sub">Submit</button> 
    <br>
    <p id="validity" aria-live=polite></p>
</div>

<script src="js/login.js"></script>

</#assign>
<#include "main.ftl">


        <#--  <h3>${output}</h3>  -->

          <#--  <form action="userqueue.ftl">
            <div class="formItem">
              Username: <input type="text" name="username", id="name">
            </div>
            <div class="formItem">
              Password: <input type="text" name="password", id="pass">
            </div>
            <input type="button" value="Submit">  -->
            <#--  , onclick="createUser()"  -->


                        <#--  <form method="POST" action="/login">  -->
                <#--  <h4 for="login">Username</h4>  -->
                <#--  <label for="text">Username here!</label><br>  -->
                <#--  <textarea name="username" id="login"
                          type="text"
                          placeholder="username here"
                          value=""></textarea>
                <h4 for="login">Password</h4>
                <textarea name="password" id="login"
                          type="text"
                          placeholder="password here"
                          value=""></textarea>
                <br>
                <input type="submit">  -->
            <#--  </form>  -->