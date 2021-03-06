<#assign content>

<h1> Recipe Search </h1>

<img id="rollingDonut" src="" class="rollingDonut">

<div class="queryBox">
    <div id="searchbox" class="queryOption">
        <div class="buttonBox">
          <input id="preferences"
            type="text"
            placeholder="Search!"
            value="">
        </div>

      <br><br><br>

      <div class="dropdown">
        <button class="dropbtn">Select Dietary Restrictions</button>
        <div class="dropdown-content">
          <input type="checkbox" id="vegan" value="" onclick="toggleNutrition('vegan')">
          <label for="vegan">Vegan</label><br>
          <input type="checkbox" id="vegetarian" value="" onclick="toggleNutrition('vegetarian')">
          <label for="vegetarian">Vegetarian</label><br>
          <input type="checkbox" id="sugar-conscious" value="" onclick="toggleNutrition('sugar-conscious')">
          <label for="sugar-conscious">Sugar Conscious</label><br>
          <input type="checkbox" id="peanut-free" value="" onclick="toggleNutrition('peanut-free')">
          <label for="peanut-free">Peanut Free</label><br>
          <input type="checkbox" id="tree-nut-free" value="" onclick="toggleNutrition('tree-nut-free')">
          <label for="tree-nut-free">Tree Nut Free</label><br>
          <input type="checkbox" id="alcohol-free" value="" onclick="toggleNutrition('alcohol-free')">
          <label for="alcohol-free">Alcohol Free</label><br>
        </div>
      </div>

      <br>

      <input id="submit_search" type="submit" value="Submit">
    </div>
</div>
<br>

<div>
  <h2 id="search_label" >Your Search: ___</h2>
</div>

<br>

  <p class="output">
  <h2 id="recititle" style="text-decoration:underline"></h2><br>
  <h2 id="isEmpty"></h2>
  <div class="bagContainer">
    <div class="shopBag" id="shoppingBag0"><img class="shoppingBag"></div>
    <div class="shopBag" id="shoppingBag1"><img class="shoppingBag"></div>
    <div class="shopBag" id="shoppingBag2"><img class="shoppingBag"></div>
    <div class="shopBag" id="shoppingBag3"><img class="shoppingBag"></div>
    <div class="shopBag" id="shoppingBag4"><img class="shoppingBag"></div>
    <div class="shopBag" id="shoppingBag5"><img class="shoppingBag"></div>
    <div class="shopBag" id="shoppingBag6"><img class="shoppingBag"></div>
    <div class="shopBag" id="shoppingBag7"><img class="shoppingBag"></div>
    <div class="shopBag" id="shoppingBag8"><img class="shoppingBag"></div>
    <div class="shopBag" id="shoppingBag9"><img class="shoppingBag"></div>
  </div>
  </p>

  <div id="container" class="container">
  </div>
  <script src="../js/search.js"></script>

</#assign>
<#include "main.ftl">