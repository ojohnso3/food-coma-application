const user = $("#user");
const pass = $("#pass");
const button = $("#sub");
const login_head = $("#login_header");

button.click(event => {
    console.log("LOGGING IN HAHAH");

    const postParameters = {
      text1: user.val(), 
      text2: pass.val()
    };

    $.post("/logged", postParameters, response => {
        console.log("LOGGING IN NOW");
    
        const output = JSON.parse(response);

        document.getElementById("validity").innerHTML = output.output;

        if (output.output == "Valid username!") {
            // window.location.href = "/search";

            login_head.innerHTML = "CHANGED HERE HAHAHAHAH";
            // login_header.attr("href", "/user");
        }
    });
});