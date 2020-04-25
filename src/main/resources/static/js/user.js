$(document).ready(() => {
    console.log("PAGE LOAD");
});

console.log("START USER");

loadSavedRecipes("HERE");

function loadSavedRecipes(userID) {

    console.log("POST USER");

    const postParameters = {
        user: userID, 
    };

    $.post("/saved", postParameters, response => {

        const output = JSON.parse(response);

        $.each(output.output, function setSaved(id, name) {
            console.log("ID HERE " + id);
            console.log("NAME HERE " + name);
            
            const url = "/recipe/" + id;

            $("#saved_list").append('<li><a href=' + url + '>' + name + '</a></li>');
        });

    });
}

const skip = $("#survey_skip");
const submit = $("#survey_submit");

skip.click(event => {
    window.location.href = "/user";
});

submit.click(event => {
    window.location.href = "/user";
});