
$(document).ready(() => {
    console.log("search console log -- hello??");

});

const preferences = $("#preferences");
const button = $("#submit");
console.log("HERE " + preferences.val());


button.click(event => {
    const postParameters = {
        //TODO: get the text inside the input box
        text1: preferences.val(),
    };

    //TODO: make a post request to the url to handle this request you set in your Main.java

    $.post("/query", postParameters, response => {
        console.log("We made a post request!");

        const output = JSON.parse(response);

        document.getElementById("validity").innerHTML = output.output;

    });

});


