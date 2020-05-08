<#assign content>

<h1>Sign Up Below!</h1>

<div class="buttonBox">
    <input id="user" class="u-full-width"
        type="text"
        placeholder="Username"
        value="">
    <br>    
    <input id="pass" class="u-full-width"
        type="text"
        placeholder="Password"
        value="">
    <br>
    <input id="pass2" class="u-full-width"
        type="text"
        placeholder="Confirm Password"
        value="">
    <br>
    <input id="bday" class="u-full-width"
        type="text"
        placeholder="Date of Birth"
        value="">
    <br>
    <button id="sub">Submit</button> 
    <br>
    <p id="validity" aria-live=polite></p>
</div>


<script src="js/signup.js"></script>



</#assign>
<#include "main.ftl">