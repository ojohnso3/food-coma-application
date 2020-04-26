console.log("start main");

// TODO: add cookies so that login/logout is maintained over all pages

const login_header = $("#login_header");
const login_button = $("#sub");
const logout_button = $("#log_out");

// login_button.click(event => {
//     login_header.innerHTML = "User";
//     login_header.attr("href", "/user");
//     window.location.href = "/user";
// });

logout_button.click(event => {
    login_header.innerHTML = "CHANGED";
    login_header.attr("href", "/user");
    window.location.href = "/home";
    console.log(login_header.attr("href"));
});