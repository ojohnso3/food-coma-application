let d = new Date();
console.log("hello??");


$(document).ready(() => {
    console.log("second print line!");
    let postParams = {
        url: window.location.href,
        username: getCookie("username")
    };
    console.log(postParams.url);
    getRecipes(postParams);
});


function getRecipes(params){
    return $.post("/recipe/recipeuri", params, response =>{
        let obj = JSON.parse(response);
        document.getElementById("title").innerHTML = " <h1><a target=_blank href=\"" + obj.URL + "\"> " + obj.title + " </a></h1>";
        console.log(obj.URL);
        document.getElementById("title").href = obj.URL;
        document.getElementById("link_note").innerHTML = " <h4><em>NOTE</em>: Click the title above for the full recipe.</h4>";

        var first = true;
        const map = obj.recipeList;
        var sortedRecipes = obj.sortedArray;
        for(let i = 0; i < obj.sortedArray.length; i++){
            console.log(sortedRecipes[i]);
        }
        $.each(sortedRecipes, function printRecipe(key, value){
            if(first = true){
                first = false;
            }
            console.log(key + ": " + value[0] + "foodcoma score: " + value[1]);
            // document.getElementById("recipes").innerHTML += " <h6 id=\"recipes\"><a href=\"/recipe/" + value[0] + "\"> " + value[1] + " foodCOMA score: " + value[2] + " </a></h6>";
            // document.getElementById("recipes").innerHTML += " <h6 class=\"side_recipes\" id=\"recipes\"><a href=\"/recipe/" + value[0] + "\"> " + value[1] + " </a></h6>";
            // document.getElementById("recipes").innerHTML += " <p class=\"score\">foodCOMA score: " + value[2] + " </p>";

            document.getElementById("recipes").innerHTML += "<div><p class=\"side_recipes\" id=\"recipes\"><a href=\"/recipe/" + value[0] + "\"> " + value[1] + " </a></p>";
            document.getElementById("recipes").innerHTML += "<p class=\"score\">foodCOMA score: " + value[2] + " </p></div><br>";
            document.getElementById("foodImage").src = obj.image;
        })
        for(let i = 0; i < obj.ingredients.length; i ++){
            document.getElementById("ingredients").innerHTML += obj.ingredients[i] + "</br>";
            console.log(obj.ingredients[i]);
        }
        document.getElementById("nutrients_title").innerHTML = "Recipe Nutrients:";
        for(let j = 0; j < obj.Nutrients.length / 3; j+=3){
            console.log("Js " + j + (j+1) + (j+2) + " fin");
            document.getElementById("nutrients").innerHTML += obj.Nutrients[j] + ": " + obj.Nutrients[j+1] + " " + obj.Nutrients[j+2] + "<br>";
        }    
        
    });

};

function compComaScores(one, two){
    var valOne = one.value[1];
    var valTwo = two.value[1];
    console.log("v1 " + valOne);
    console.log("v2 " + valTwo);
}
