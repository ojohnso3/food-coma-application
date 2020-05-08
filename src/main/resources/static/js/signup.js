const user = $("#user");
const pass1 = $("#pass");
const pass2 = $("#pass2");
// const birth = $("#bday");
const button = $("#sign_sub");

button.click(event => {
    const postParameters = {
        user: user.val(), 
        pass1: pass1.val(),
        pass2: pass2.val(),
    };

    $.post("/signed", postParameters, response => {
        console.log("MAKING REQUEST");

        const output = JSON.parse(response);
        document.getElementById("sign_validity").innerHTML = output.output;


        if (output.output == "Successful Sign-up!") {
            console.log("USER NAME HERE " + user.val());
            checkCookie(user.val());
            window.location.href = "/survey";
        }
    });
});