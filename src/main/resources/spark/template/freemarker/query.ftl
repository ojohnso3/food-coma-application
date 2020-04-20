<#assign content>

<h1> Recipe Search </h1>


<div id="main">
<br>
<form> <#-- action="/action_page.php" -->
  <input type="checkbox" name="checkoff" value="Food">
  <label for="checkoff">Some checkoff item</label><br>
</form>

<br>
<p> Recipe!
<form method="GET" action="/results">
  <label for="text">Put in preferences for Recipe Search</label><br>
  <textarea name="text" id="text"></textarea><br>
  <input type="submit">
</form>
</div>
  <p class="output">

  <h3>List of recipes:</h3>
  <#list recipeList as r>
    <h6 id="recipes"><a href="/recipe/${r.getUri()}">${r.getUri()}</a></h6>
  </#list>

  </p>


</#assign>
<#include "main.ftl">