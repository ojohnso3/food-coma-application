alertCookie();

var username = getCookie("username");
loadSavedRecipes(username);


function loadSavedRecipes(userID) {
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

const logout = $("#logoutbutton");

logout.click(event => {
    console.log("LOGGING OUT YAYAYYAYYA");
    deleteCookie();
    window.location.href = "/home";
});