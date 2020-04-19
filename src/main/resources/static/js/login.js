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

// function setValue(val) {
//     output = val;
// }

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

      const output = JSON.parse(response);
      console.log("OUT " + output.output);

      document.getElementById("validity").innerHTML = output.output;

      if (output.output == "Valid username!") {
        console.log("yayayayayayayyayayayay");
        window.location.href = "/foodCOMA";
        }
    });



    console.log("end!");
// document.getElementById("validity").innerHTML = output.output;

});

