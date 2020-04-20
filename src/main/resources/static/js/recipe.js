let d = new Date();
console.log("hello??");


$(document).ready(() => {
    console.log("second print line!");
    $.get("/recipe/:recipeuri", response => {
        console.log("We made a get request!");
        console.log(response);
        const output = JSON.parse(response);

        // Do something with the response here
    });
    // document.getElementById("validity").innerHTML = "START";
});


