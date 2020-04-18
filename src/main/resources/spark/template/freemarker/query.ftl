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
<form method="GET" action="/recipe">
  <label for="text">Put in preferences for Recipe Search</label><br>
  <textarea name="text" id="text"></textarea><br>
  <input type="submit">
</form>
</p>
  <p class="output">

  <#list recipeList as r>
    <h6><a href="http://localhost:4567/recipe/${r.getURI()}" ${r.getURI()}</a></h6>

  </#list>

  </p>


<br><br>


<br><br>

</#assign>
<#include "main.ftl">