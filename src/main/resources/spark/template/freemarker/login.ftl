<#assign content>

<h1>Join Us!</h1>


<div class="queryBox">
    <div class="queryOption">
        <h2> SIGN-IN </h2>
        <input id="log_user" class="logbox"
            type="text"
            placeholder="Username"
            value="">
        <br>    
        <input id="log_pass" class="logbox"
            type="password"
            placeholder="Password"
            value="">
        <input id="log_sub" type="submit" value="Sign In!">
        <br>
        <p id="log_validity" aria-live=polite></p>
    </div>

    <div>
        <h2 id="or" >OR</h2>
    </div>

    <div class="queryOption">
        <h2> SIGN-UP </h2>
        <input id="user" class="logbox"
            type="text"
            placeholder="Username"
            value="">
        <br>    
        <input id="pass" class="logbox"
            type="password"
            placeholder="Password"
            value="">
        <br>
        <input id="pass2" class="logbox"
            type="password"
            placeholder="Confirm Password"
            value="">
        <br>
        <input id="sign_sub" type="submit" value="Sign Up!">
        <br>
        <p id="sign_validity" aria-live=polite></p>
    </div>
</div>
<br><br>

<script src="js/login.js"></script>
<script src="js/signup.js"></script>

</#assign>
<#include "main.ftl">
