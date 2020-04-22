$(document).ready(() => {
    console.log("second print line!");
    // document.getElementById("validity").innerHTML = "START";
});

const skip = $("#survey_skip");
const submit = $("#survey_submit");

skip.click(event => {
    window.location.href = "/search";
});

submit.click(event => {
    window.location.href = "/search";
});