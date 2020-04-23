<#assign content>

<h1>Sign Up Below!</h1>

<div class="buttonBox">
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
    <p id="validity" aria-live=polite></p>
</div>


<script src="js/signup.js"></script>



</#assign>
<#include "main.ftl">