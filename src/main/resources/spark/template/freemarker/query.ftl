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


<br><br>

<h3>${recipes}</h3>

<br><br>

</#assign>
<#include "main.ftl">