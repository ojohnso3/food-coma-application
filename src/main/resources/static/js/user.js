// alertCookie();

var username = getCookie("username");
loadSavedRecipes(username);


function loadSavedRecipes(userID) {
    const postParameters = {
        user: userID, 
    };
    $.post("/saved", postParameters, response => {
        const output = JSON.parse(response);


        $.each(output.output, function setSaved(id, name) {
            document.getElementById("recipes_saved").innerHTML += "<h4 id=\"recipes\"><a href=\"/recipe/" + id + "\"> " + name + " </a></h4>";
        })

    });
}

const logout = $("#logoutbutton");
const search = $("#searchbutton");

logout.click(event => {
    deleteCookie();
    window.location.href = "/login";
});

search.click(event => {
    window.location.href = "/search";
});