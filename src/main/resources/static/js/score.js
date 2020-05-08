alert(getCookie("username"));

console.log("SCORE HERE");
console.log(getCookie("username"));

$(document).ready(function(){
    getWeights();
});

function getWeights() {
    const postParameters = {
        username: getCookie("username")
    };

    $.post("/score", postParameters, response => {

        const output = JSON.parse(response);
        console.log("SCOR POST");

        if (output.output.length == 0) {
            document.getElementById("weights").innerHTML += " <h4 id=\"all_weights\">Weights are unable to be processed at this time.</h4>";
        }

        for(let i = 0; i < (output.output.length); i+=2){
            console.log(output.output[i]);
            console.log(output.output[i+1]);
            document.getElementById("weights").innerHTML += " <h4 id=\"all_weights\"> " + output.output[i] + ": " + output.output[i+1] + "</h4>";
        }
    });
};
