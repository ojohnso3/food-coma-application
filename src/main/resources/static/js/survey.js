$(document).ready(() => {
    console.log("second print line!");
    // document.getElementById("validity").innerHTML = "START";
});

const skip = $("#survey_skip");
const submit = $("#survey_submit");
const feedback = $("#last_q");

skip.click(event => {
    window.location.href = "/user";
});

submit.click(event => {

    // const calcium = $('#calcium:checked').val();
    // const carbs = $('#carbs:checked').val();
    // const cholestorol = $('#cholestorol:checked').val();
    // const monounsaturated = $('#monounsaturated:checked').val();
    // const polysaturated = $('#polysaturated:checked').val();
    // const sugar = $('#sugar:checked').val();
    // const fat = $('#fat:checked').val();
    // const trans = $('#trans:checked').val();
    // const iron = $('#iron:checked').val();
    // const fiber = $('#fiber:checked').val();
    // const folate = $('#folate:checked').val();

    // const saturated = $('#saturated:checked').val();
    const calcium = $("#calcium").is(":checked");
    const carbs = $("#carbs").is(":checked");
    const energy = $("#energy").is(":checked");

    const postParameters = {
        username: getCookie("username"),
        feedback: feedback.val(),
        Calcium: calcium,
        Carbs: carbs,
        Energy: energy
    };

    $.post("/survey_post", postParameters, response => {
    
        const output = JSON.parse(response);

        document.getElementById("survey_validity").innerHTML = output.output;

        if (output.output == "Valid Survey!") {
            window.location.href = "/user";
        }
    });
});