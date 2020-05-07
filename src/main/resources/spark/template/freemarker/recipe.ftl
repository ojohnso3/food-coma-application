<#assign content>

    <h1 class="recpTitle" id = "title" aria-live=polite><a href="http://www.facebook.com"></a></h1>
    <script src="../js/recipe.js"></script>
    <div id="container" class="container">

    </div>

    <div class="row">
        <div class="column">
            <h4><em>Note</em>: click title above to view full recipe.</h4>
            <h2 style="text-decoration: underline">Recipe Ingredients: </h2>
            <p id="ingredients" aria-live=polite></p>
            <img class=recipeFood src="" id="foodImage">
            <br>
            <h4 style="text-decoration: underline" id="nutrients_title"></h4>
            <p id="nutrients"></p>
        </div>

        <div class="column2">
            <h3 style="text-decoration: underline">Other Recipes to Consider:</h3>
            <h2 id="recipes" aria-live=polite></h2>
        </div>

    </div>

</#assign>
<#include "main.ftl">