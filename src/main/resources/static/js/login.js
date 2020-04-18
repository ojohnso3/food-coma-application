let d = new Date();
alert("Today's date is " + d);

console.log("first print line!");

$(document).ready(() => {
    console.log("second print line!");
    document.getElementById("validity").innerHTML = "START";
});

const user = $("#user");
const pass = $("#pass");
const button = $("#sub");
let output;

// const user = $("#username");
// const pass = $("#password");

// console.log(user);
// console.log(pass);

console.log("works!");


// input.keyup(event => {
button.click(event => {

    console.log("start!");

    const postParameters = {
      //TODO: get the text inside the input box
      text1: user.val(), 
      text2: pass.val()
    };

    console.log("middle!");

    //TODO: make a post request to the url to handle this request you set in your Main.java

    $.post("/login", postParameters, response => {
      console.log("We made a post request!");
      console.log("RESP" + response);
      // Do something with the response here

      output = JSON.parse(response);
      console.log("OUT " + output.output);

      document.getElementById("validity").innerHTML = output.output;
    });

    console.log("end!");
// document.getElementById("validity").innerHTML = output.output;

});

if (condition) {
    //  block of code to be executed if the condition is true
} 