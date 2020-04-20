// let d = new Date();
// alert("Today's date is " + d);


console.log("first print line!");

$(document).ready(() => {
    console.log("second print line!");
    // document.getElementById("validity").innerHTML = "START";
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


// input.keyup(event => {
button.click(event => {

    const postParameters = {
      //TODO: get the text inside the input box
      text1: user.val(), 
      text2: pass.val()
    };

    //TODO: make a post request to the url to handle this request you set in your Main.java

    $.post("/login", postParameters, response => {
      console.log("We made a post request!");

      const output = JSON.parse(response);

      document.getElementById("validity").innerHTML = output.output;

      if (output.output == "Valid username!") {
        window.location.href = "/foodCOMA";
      }
    });



    console.log("end!");
// document.getElementById("validity").innerHTML = output.output;

});

