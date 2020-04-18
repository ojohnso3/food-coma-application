let d = new Date();
alert("Today's date is " + d);

console.log("first print line!");

$(document).ready(() => {
    console.log("second print line!");
    document.getElementById("validity").innerHTML = "START";
});

const input = $("#login");
const button = $("#sub");

// const user = $("#username");
// const pass = $("#password");

// console.log(user);
// console.log(pass);

console.log("works!");


// input.keyup(event => {
button.click(event => {

    const postParameters = {
      //TODO: get the text inside the input box
      text: input.val()
    };

    //TODO: make a post request to the url to handle this request you set in your Main.java

    $.post("/login", postParameters, response => {
      console.log("We made a post request!");
      console.log("RESP" + response);
      // Do something with the response here

      const output = JSON.parse(response);
      console.log("OUT " + output.output);

      document.getElementById("validity").innerHTML = output.output;
});

// document.getElementById("validity").innerHTML = output.output;

});