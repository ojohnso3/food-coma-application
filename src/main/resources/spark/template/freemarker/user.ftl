<#assign content>

<h1>User Profile</h1>


<div class="queryBox">
    <div class="queryOption">
        <h2>Find New Recipes!</h2>
            <input id="searchbutton" type="submit" value="Search">
    </div>
    <div class="queryOption">
        <h2>Time to Go? Sad :(</h2>
            <input id="logoutbutton" type="submit" value="Log out">
    </div>
</div>

<div class="queryBox">
    <div class="queryOption" id="saved_recipes">
        <h2 style="text-decoration: underline">Your Previous Recipes</h2>
        <h3 id="recipes_saved" aria-live=polite></h3>
    </div>
</div>


<script src="js/user.js"></script>

</#assign>
<#include "main.ftl">