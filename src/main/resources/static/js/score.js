console.log("SCORE HERE");
console.log(getCookie("username"));

$(document).ready(function(){
    getNutrients();
});

function getNutrients() {
    const postParameters = {
        // username: getCookie("username")
    };

    $.post("/score", postParameters, response => {

        const output = JSON.parse(response);
        console.log("SCORE POST");

        if (output.output.length == 0) {
            document.getElementById("weights").innerHTML += " <h4 id=\"all_weights\">Nutrients are unable to be processed at this time.</h4>";
        }
        for(let i = 0; i < (output.output.length); i++){
            document.getElementById("weights").innerHTML += " <h4 id=\"all_weights\"> " + output.output[i] + "</h4>";
        }
    });
};

