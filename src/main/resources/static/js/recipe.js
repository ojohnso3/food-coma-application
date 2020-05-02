let d = new Date();
console.log("hello??");


$(document).ready(() => {
    console.log("second print line!");
    let postParams = {
        url: window.location.href,
        // username: getCookie("username")
    };
    console.log(postParams.url);
    getRecipes(postParams);

    // $.post("/recipe/recipeuri", postParams, response => {
    //     console.log("We made a get request!");
    //     console.log(postParams.url);
    //     // console.log(response);
    //     // const output = JSON.parse(response);
    //     // Do something with the response here
    // });
    // document.getElementById("validity").innerHTML = "START";
});


function getRecipes(params){
    return $.post("/recipe/recipeuri", params, response =>{
        let obj = JSON.parse(response);
        document.getElementById("title").innerHTML = " <h1><a target=_blank href=\"" + obj.URL + "\"> " + obj.title + " </a></h1>";
        console.log(obj.URL);
        document.getElementById("title").href = obj.URL;
        $.each(obj.recipeList, function printRecipe(key, value){
            console.log(key + ": " + value[0]);
            document.getElementById("recipes").innerHTML += " <h6 id=\"recipes\"><a href=\"/recipe/" + key + "\"> " + value[0] + " </a></h6>";
            document.getElementById("foodImage").src = obj.image;
        })
        for(let i = 0; i < obj.ingredients.length; i ++){
            document.getElementById("ingredients").innerHTML += obj.ingredients[i] + "</br>";
            console.log(obj.ingredients[i]);
        }
            // console.log(ingredients(key));

    });
};
//
// function newRecipe(){
//     return "hello";
// }


