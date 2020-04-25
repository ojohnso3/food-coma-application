<#assign content>

<h1>User Profile</h1>

<#--  <div class="user_header">
    <button id="saved">Saved Recipes</button>
    <button id="manage">Manage Account</button> 
    <button id="log_out">Log Out</button> 
</div>
<br>  -->

<div class="queryBox">
    <div class="queryOption">
        <h2>Find New Recipes!</h2>
        <form method="GET" action="/connect">
            <#--  <h4 for="text">Search</h4>
            <textarea name="actorOneText" id="text" placeholder="Search here!"></textarea>  -->
            <#--  <h4 for="text">Actor Two</h4>
            <textarea name="actorTwoText" id="text"></textarea>  -->
            <input type="submit" value="Search!">
        </form>
    </div>
    <div class="queryOption">
        <h2>Time to Go? Sad :(</h2>
        <form method="GET" action="/database">
            <input type="submit" value="Log out">
        </form>
    </div>
</div>

<div class="queryBox">
    <div class="queryOption" id="saved_recipes">
        <h2>Your Saved Recipes</h2>
        <ul id="saved_list">
        </ul>
    </div>
</div>


<script src="js/user.js"></script>

</#assign>
<#include "main.ftl">