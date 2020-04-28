
$(document).ready(() => {
    console.log("search console log -- hello??");

});

const preferences = $("#preferences");
const button = $("#submit");
console.log("HERE " + preferences.val());

$(document).ready(function(){
    $('#preferences').keypress(function(e){
        if(e.keyCode==13)
            $('#submit').click();
    });
});

button.click(event => {
    var donut = document.getElementById("rollingDonut");
    var newDonut = donut.cloneNode(true);
    donut.parentNode.replaceChild(newDonut, donut);
    document.getElementById("rollingDonut").src = "https://i.postimg.cc/c4XzqfF1/image.png";

    const postParameters = {
        //TODO: get the text inside the input box
        prefs: preferences.val(),
    };
    //TODO: make a post request to the url to handle this request you set in your Main.java

    $.post("/search", postParameters, response => {
        console.log("We made a post request!");
        const output = JSON.parse(response);
        // document.getElementById("#preferences").innerHTML = "";
        let i = 0;
        $.each(output.simpleRecipeList, function printRecipe(index, key){
            // document.getElementById("container").innerHTML +=
            console.log("Inside each statement simpleRecipeList");
            console.log("Index: " + index);
            console.log("Key: " + key);
            console.log("Key 0: " + key[0]);
            console.log("Key 1: " + key[1]);

            // document.getElementById("container").innerHTML += "Name: " + index + " URL: " + key[0];
            // document.getElementById("container").innerHTML += "HELLO?";
            document.getElementById("container").innerHTML += "<h6><a href = \"recipe/" + key[1] + "\">" + index + " </a></h6>";

            console.log("shoppingBag" + i);
            var shoppingBagHtml = "<a class=\"recipeText\" href = \"recipe/" + key[1] + "\">" + index + " </a>"
            shoppingBagHtml.toString();
            // document.getElementById("shoppingBag" + i).innerHTML += shoppingBagHtml;
            document.getElementById("shoppingBag" + i).innerHTML += "<a class=\"recipeText\" href = \"recipe/" + key[1] + "\">" + index + " </a>";
            // document.getElementById("shoppingBag" + i).innerHTML += "<a class=\"recipeText\" href = \"recipe/" + key[1] + "\">" + "hello??????????????????" + " </a>";

            i++;
            // document.getElementById("container").innerHTML += " <h6><a href=\"/recipe/" + key[1] + "\"> " + index + " </a></h6>";

        });
        // for(i = 0; i < output.recipes.length; i++){
        //     console.log(output.recipes[i].getLabel());
        // }
    });

});


