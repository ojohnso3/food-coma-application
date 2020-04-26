const log_user = $("#log_user");
const log_pass = $("#log_pass");
const submit = $("#log_sub");

submit.click(event => {

    const postParameters = {
      text1: log_user.val(), 
      text2: log_pass.val()
    };

    $.post("/logged", postParameters, response => {
    
        const output = JSON.parse(response);

        document.getElementById("log_validity").innerHTML = output.output;

        if (output.output == "Valid Login!") {
            window.location.href = "/user";
            checkCookie(log_user.val());
            // console.log("WORKING");
            // document.getElementById("login_header").innerHTML = "User";
            // login_head.innerHTML = "CHANGED HERE";
            // login_header.attr("href", "/user");
        }
    });
});