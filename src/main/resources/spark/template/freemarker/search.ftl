<#assign content>

<h1> Recipe Search </h1>

<#--  <div class="queryBox">
    <div class="queryOption">
        <h2>Search here!</h2>
        <form>
            <h4 for="text">Recipe Search</h4>
            <textarea name="searchText" id="preferences" placeholder="Search here!"></textarea>
            <input id="submit" type="submit" value="Search!">
        </form>
    </div>
</div>  -->

<div class="buttonBox">
  <input id="preferences"
    type="text"
    placeholder="Search!"
    value="">
  </input>
  <br>
  <button id="submit">Submit</button>
  <br>
</div>

<form> <#-- action="/action_page.php" -->
  <input type="checkbox" name="checkoff" value="Food">
  <label for="checkoff">Some checkoff item</label><br>
</form>

  <p class="output">

  <h3>List of recipes:</h3>

  </p>
  <div id="container" class="container">
  </div>
  <script src="../js/search.js"></script>


</#assign>
<#include "main.ftl">