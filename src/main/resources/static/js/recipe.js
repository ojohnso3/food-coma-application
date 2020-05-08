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
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[0] + ": " + obj.Nutrients[1] + " " + obj.Nutrients[2] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[3] + ": " + obj.Nutrients[4] + " " + obj.Nutrients[5] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[6] + ": " + obj.Nutrients[7] + " " + obj.Nutrients[8] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[9] + ": " + obj.Nutrients[10] + " " + obj.Nutrients[11] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[12] + ": " + obj.Nutrients[13] + " " + obj.Nutrients[14] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[15] + ": " + obj.Nutrients[16] + " " + obj.Nutrients[17] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[18] + ": " + obj.Nutrients[19] + " " + obj.Nutrients[20] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[21] + ": " + obj.Nutrients[22] + " " + obj.Nutrients[23] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[24] + ": " + obj.Nutrients[25] + " " + obj.Nutrients[26] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[27] + ": " + obj.Nutrients[28] + " " + obj.Nutrients[29] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[30] + ": " + obj.Nutrients[31] + " " + obj.Nutrients[32] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[33] + ": " + obj.Nutrients[34] + " " + obj.Nutrients[35] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[36] + ": " + obj.Nutrients[37] + " " + obj.Nutrients[38] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[39] + ": " + obj.Nutrients[40] + " " + obj.Nutrients[41] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[42] + ": " + obj.Nutrients[43] + " " + obj.Nutrients[44] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[45] + ": " + obj.Nutrients[46] + " " + obj.Nutrients[47] + "<br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[48] + ": " + obj.Nutrients[49] + " " + obj.Nutrients[50] + "<br>";
    });

};

function compComaScores(one, two){
    var valOne = one.value[1];
    var valTwo = two.value[1];
    console.log("v1 " + valOne);
    console.log("v2 " + valTwo);
}


        // document.getElementById("nutrients").innerHTML += obj.Nutrients[0] + ": " + obj.Nutrients[1] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[2] + ": " + obj.Nutrients[3] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[4] + ": " + obj.Nutrients[5] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[6] + ": " + obj.Nutrients[7] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[8] + ": " + obj.Nutrients[9] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[10] + ": " + obj.Nutrients[11] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[12] + ": " + obj.Nutrients[13] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[14] + ": " + obj.Nutrients[15] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[16] + ": " + obj.Nutrients[17] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[18] + ": " + obj.Nutrients[19] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[20] + ": " + obj.Nutrients[21] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[22] + ": " + obj.Nutrients[23] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[24] + ": " + obj.Nutrients[25] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[26] + ": " + obj.Nutrients[27] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[28] + ": " + obj.Nutrients[29] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[30] + ": " + obj.Nutrients[31] + " kcal <br>";
        // document.getElementById("nutrients").innerHTML += obj.Nutrients[32] + ": " + obj.Nutrients[33] + " kcal <br>";