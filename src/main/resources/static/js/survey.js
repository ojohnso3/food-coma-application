$(document).ready(() => {
    console.log("second print line!");
    // document.getElementById("validity").innerHTML = "START";
});

const skip = $("#survey_sub");

skip.click(event => {
    window.location.href = "/query";
});