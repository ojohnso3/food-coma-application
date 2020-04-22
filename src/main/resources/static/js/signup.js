$(document).ready(() => {
    console.log("second print line!");
    // document.getElementById("validity").innerHTML = "START";
});

const user = $("#user");
const pass1 = $("#pass");
const pass2 = $("#pass");
const birth = $("#bday");

const button = $("#sub");


// input.keyup(event => {
button.click(event => {

    const postParameters = {
      //TODO: get the text inside the input box
      user: user.val(), 
      pass1: pass1.val(),
      pass2: pass2.val(),
      birth: birth.val()
    };

    //TODO: make a post request to the url to handle this request you set in your Main.java

    $.post("/login", postParameters, response => {
      console.log("We made a post request!");

      const output = JSON.parse(response);

      document.getElementById("validity").innerHTML = output.output;

      if (output.output == "Valid signup!") {
        window.location.href = "/survey";
      }
    });



    console.log("end!");
// document.getElementById("validity").innerHTML = output.output;

});