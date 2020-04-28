<#assign content>

<h1>Join Us!</h1>


<div class="queryBox">
    <div class="queryOption">
        <h2> SIGN-IN </h2>
        <input id="log_user" class="u-full-width"
            type="text"
            placeholder="Username"
            value=""></input>
        <br>    
        <input id="log_pass" class="u-full-width"
            type="password"
            placeholder="Password"
            value=""></input>
        <input id="log_sub" type="submit" value="Sign In!">
        <br>
        <p id="log_validity" aria-live=polite></p>
    </div>

    <div>
        <h2>OR</h2>
    </div>

    <div class="queryOption">
        <h2> SIGN-UP </h2>
        <input id="user" class="u-full-width"
            type="text"
            placeholder="Username"
            value=""></input>
        <br>    
        <input id="pass" class="u-full-width"
            type="password"
            placeholder="Password"
            value=""></input>
        <br>
        <input id="pass2" class="u-full-width"
            type="password"
            placeholder="Confirm Password"
            value=""></input>
        <br>
        <#--  <input id="bday" class="u-full-width"
            type="text"
            placeholder="Date of Birth"
            value=""></input>
        <br>  -->
        <input id="sign_sub" type="submit" value="Sign Up!">
        <br>
        <p id="sign_validity" aria-live=polite></p>
    </div>
</div>

<#--  <div class="signContainer">
    <div class="signBox">
        <h2> LOG-IN </h2>
        <input id="log_user" class="u-full-width"
            type="text"
            placeholder="username"
            value=""></input>
        <br>    
        <input id="log_pass" class="u-full-width"
            type="text"
            placeholder="password"
            value=""></input>
        <br>
        <button id="sub">Submit</button> 
        <br>
        <p id="log_validity" aria-live=polite></p>
    </div>

    <div class="signBox">
        <h2> SIGN-UP </h2>
        <input id="user" class="u-full-width"
            type="text"
            placeholder="Username"
            value=""></input>
        <br>    
        <input id="pass" class="u-full-width"
            type="text"
            placeholder="Password"
            value=""></input>
        <br>
        <input id="pass2" class="u-full-width"
            type="text"
            placeholder="Confirm Password"
            value=""></input>
        <br>
        <input id="bday" class="u-full-width"
            type="text"
            placeholder="Date of Birth"
            value=""></input>
        <br>
        <button id="sub">Submit</button> 
        <br>
        <p id="signvalidity" aria-live=polite></p>
    </div>
</div>  -->

<script src="js/login.js"></script>
<script src="js/signup.js"></script>

</#assign>
<#include "main.ftl">





<#--  OLD BELOW  -->

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
