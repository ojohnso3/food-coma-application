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
        <h2>input timdb data</h2>
        <form method="GET" action="/connect">
            <h4 for="text">Actor One</h4>
            <textarea name="actorOneText" id="text" placeholder="Search here!"></textarea>
            <#--  <h4 for="text">Actor Two</h4>
            <textarea name="actorTwoText" id="text"></textarea>  -->
            <input type="submit" value="Search!">
        </form>
    </div>
    <div class="queryOption">
        <h2>click below to automatically load database</h2>
        <form method="GET" action="/database">
            <input type="submit" value="Log out">
        </form>
    </div>
</div>

<div class="queryBox">
    <div class="queryOption">
        <h2>click below to automatically load database</h2>
        <form method="GET" action="/database">
            <input type="submit" value="Log out">
        </form>
    </div>
    <div class="queryOption">
        <h2>input timdb data</h2>
        <form method="GET" action="/connect">
            <h4 for="text">Actor One</h4>
            <textarea name="actorOneText" id="text" placeholder="Search here!"></textarea>
            <#--  <h4 for="text">Actor Two</h4>
            <textarea name="actorTwoText" id="text"></textarea>  -->
            <input type="submit" value="Search!">
        </form>
    </div>
</div>

<div class="user_header">
    <p>HI</p> 
</div>


</#assign>
<#include "main.ftl">