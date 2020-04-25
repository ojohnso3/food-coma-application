const user = $("#user");
const pass = $("#pass");
const button = $("#sub");

button.click(event => {

    const postParameters = {
      text1: user.val(), 
      text2: pass.val()
    };

    $.post("/logged", postParameters, response => {
        console.log("LOGGING IN NOW");
    
        const output = JSON.parse(response);

        document.getElementById("validity").innerHTML = output.output;

        if (output.output == "Valid username!") {
            window.location.href = "/search";

            // login_header.innerHTML = "User";
            // login_header.attr("href", "/user");
        }
    });
});