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

    const calcium = $("#calcium").is(":checked");
    const carbs = $("#carbs").is(":checked");
    const cholesterol = $("#cholesterol").is(":checked");
    const monounsaturated = $("#monounsaturated").is(":checked");
    const polyunsaturated = $("#polyunsaturated").is(":checked");
    const sugars = $("#sugars").is(":checked");
    const fat = $("#fat").is(":checked");
    const trans = $("#trans").is(":checked");
    const iron = $("#iron").is(":checked");
    const fiber = $("#fiber").is(":checked");
    const potassium = $("#potassium").is(":checked");
    const magnesium = $("#magnesium").is(":checked");
    const sodium = $("#sodium").is(":checked");
    const vitaminB6 = $("#vitaminB6").is(":checked");
    const energy = $("#energy").is(":checked");
    const protein = $("#protein").is(":checked");
    const sugarsadded = $("#sugarsadded").is(":checked");
    const saturated = $("#saturated").is(":checked");
    const vitaminE = $("#vitaminE").is(":checked");
    const vitaminA = $("#vitaminA").is(":checked");
    const vitaminB12 = $("#vitaminB12").is(":checked");
    const vitaminC = $("#vitaminC").is(":checked");
    const vitaminD = $("#vitaminD").is(":checked");
    const vitaminK = $("#vitaminK").is(":checked");

    const postParameters = {
        username: getCookie("username"),
        feedback: feedback.val(),
        Calcium: calcium,
        Carbs: carbs,
        Cholesterol: cholesterol,
        Monounsaturated: monounsaturated,
        Polyunsaturated: polyunsaturated,
        Sugars: sugars,
        Fat: fat,
        Trans: trans,
        Iron: iron,
        Fiber: fiber,
        Potassium: potassium,
        Magnesium: magnesium,
        Sodium: sodium,
        VitaminB6: vitaminB6,
        Energy: energy,
        Protein: protein,
        Sugarsadded: sugarsadded,
        Saturated: saturated,
        VitaminE: vitaminE,
        VitaminA: vitaminA,
        VitaminB12: vitaminB12,
        VitaminC: vitaminC,
        VitaminD: vitaminD,
        VitaminK: vitaminK
    };

    $.post("/survey_post", postParameters, response => {
    
        const output = JSON.parse(response);

        document.getElementById("survey_validity").innerHTML = output.output;

        if (output.output == "Valid Survey!") {
            window.location.href = "/user";
        }
    });
});