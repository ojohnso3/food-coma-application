console.log("start main");

const login_header = $("#login_header");
const login_button = $("#sub");
const logout_button = $("#log_out");


logout_button.click(event => {
    login_header.innerHTML = "CHANGED";
    login_header.attr("href", "/user");
    window.location.href = "/home";
    console.log(login_header.attr("href"));
});