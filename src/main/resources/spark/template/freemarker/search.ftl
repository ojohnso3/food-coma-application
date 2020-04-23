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
  <br>
<#--<form method="GET" action="/search">-->
<#--  <label for="text">Put in preferences for Recipe Search</label><br>-->
<#--  <textarea name="preferences" id="preferences"></textarea><br>-->
<#--  <input type="submit">-->
<#--</form>-->

</div>

  <div class="buttonBox">
    <input id="preferences" class="u-full-width"
           type="text"
           placeholder="username"
           value=""></input>
    <br>

    <br>
    <button id="submit">Submit</button>
    <br>
  </div>

  <p class="output">

  <h3>List of recipes:</h3>

  </p>
  <div id="container" class="container">
  </div>
  <script src="../js/search.js"></script>


</#assign>
<#include "main.ftl">