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

  <img id="rollingDonut" src="" class="rollingDonut">

<div class="buttonBox">
  <input id="preferences"
    type="text"
    placeholder="Search!"
    value="">

  </input>
  <br>
  <button type="submit" id="submit">Submit</button>
  <br>
</div>



  <p class="output">
  <h3>List of recipes:</h3>
  <div class="bagContainer">
  <div class="shopBag" id="shoppingBag0"><img src="https://i.postimg.cc/FK87G91b/bag-161440-1280.png" class="shoppingBag"></div>
  <div class="shopBag" id="shoppingBag1"><img src="https://i.postimg.cc/FK87G91b/bag-161440-1280.png" class="shoppingBag"></div>
  <div class="shopBag" id="shoppingBag2"><img src="https://i.postimg.cc/FK87G91b/bag-161440-1280.png" class="shoppingBag"></div>
  <div class="shopBag" id="shoppingBag3"><img src="https://i.postimg.cc/FK87G91b/bag-161440-1280.png" class="shoppingBag"></div>
  <div class="shopBag" id="shoppingBag4"><img src="https://i.postimg.cc/FK87G91b/bag-161440-1280.png" class="shoppingBag"></div>
  <div class="shopBag" id="shoppingBag5"><img src="https://i.postimg.cc/FK87G91b/bag-161440-1280.png" class="shoppingBag"></div>
  <div class="shopBag" id="shoppingBag6"><img src="https://i.postimg.cc/FK87G91b/bag-161440-1280.png" class="shoppingBag"></div>
  <div class="shopBag" id="shoppingBag7"><img src="https://i.postimg.cc/FK87G91b/bag-161440-1280.png" class="shoppingBag"></div>
  <div class="shopBag" id="shoppingBag8"><img src="https://i.postimg.cc/FK87G91b/bag-161440-1280.png" class="shoppingBag"></div>
  <div class="shopBag" id="shoppingBag9"><img src="https://i.postimg.cc/FK87G91b/bag-161440-1280.png" class="shoppingBag"></div>
  </div>



  </p>
  <div id="container" class="container">
  </div>
  <script src="../js/search.js"></script>


</#assign>
<#include "main.ftl">