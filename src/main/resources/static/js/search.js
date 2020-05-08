
$(document).ready(() => {
    var donut = document.getElementById("rollingDonut");
    var newDonut = donut.cloneNode(true);
    donut.parentNode.replaceChild(newDonut, donut);
    document.getElementById("rollingDonut").src = "https://i.postimg.cc/c4XzqfF1/image.png";
    document.getElementById("rollingDonut").style.animationPlayState="paused";
});

const preferences = $("#preferences");
const button = $("#submit_search");
console.log("HERE " + preferences.val());

$(document).ready(function(){
    $('#preferences').keypress(function(e){
        if(e.keyCode==13)
            $('#submit').click();
    });
});

button.click(event => {
    if (getCookie("username") == "") {
        alert("Please make sure you're signed in before making searches!")
        return "";
    } else {
        return makeQuery();
    }
});


function makeQuery() {
    document.getElementById("rollingDonut").style.animationPlayState="running";
    document.getElementById("search_label").innerHTML = "Your Search: " + preferences.val();
    const vegan = $("#vegan").is(":checked");
    const vegetarian = $("#vegetarian").is(":checked");
    const sugarconscious = $("#sugar-conscious").is(":checked");
    const peanutfree = $("#peanut-free").is(":checked");
    const treenutfree = $("#tree-nut-free").is(":checked");
    const alcoholfree = $("#alcohol-free").is(":checked");

    const postParameters = {
        prefs: preferences.val(),
        username: getCookie("username"),
        vg: vegan,
        veg: vegetarian,
        sug: sugarconscious,
        pf: peanutfree,
        tf: treenutfree,
        af: alcoholfree
    };

    $.post("/search", postParameters, response => {

        const output = JSON.parse(response);
        let i = 0;
        $.each(output.simpleRecipeList, function printRecipe(index, key){
            // document.getElementById("container").innerHTML +=
            console.log("Inside each statement simpleRecipeList");
            console.log("Index: " + index);
            console.log("Key: " + key);
            console.log("Key 0: " + key[0]);
            console.log("Key 1: " + key[1]);
            if(i < 10){
                console.log("shoppingBag" + i);
                var fullShopBagHtml = "<img src=\"https://i.postimg.cc/FK87G91b/bag-161440-1280.png\" class=\"shoppingBag\"><a class=\"recipeText\" href = \"recipe/" + key[1] + "\">" + index + " </a>"
                document.getElementById("shoppingBag" + i).innerHTML = fullShopBagHtml.toString();
            }
            i++;
        });
        if(output.recipes.length == 0){
            for(let i = 0; i < 10; i++){
                var fullShopBagHtml = "<img class=\"shoppingBag\"><a class=\"recipeText\" </a>"
                document.getElementById("shoppingBag" + i).innerHTML = fullShopBagHtml.toString();
            }
            document.getElementById("isEmpty").innerHTML = "No search results :(";
        } else if (output.recipes.length != 0){
            document.getElementById("recititle").innerHTML = "List of recipes:";
            document.getElementById("isEmpty").innerHTML = "";
        }
        document.getElementById("rollingDonut").style.animationPlayState="paused";
    });
};
