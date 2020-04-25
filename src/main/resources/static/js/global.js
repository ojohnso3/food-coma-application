// declare cookie functions

function alertCookie() {
  alert(document.cookie);
}


// complicated

function setCookie(cookieName, value, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+ d.toUTCString();
    document.cookie = cookieName + "=" + value + ";" + expires + ";path=/";
}

function getCookie(cookieName) {
    var name = cookieName + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var cookieArray = decodedCookie.split(';');
    for(var i = 0; i < cookieArray.length; i++) {
        var cookie = cookieArray[i];
        while (cookie.charAt(0) == ' ') {
        cookie = cookie.substring(1);
        }
        if (cookie.indexOf(name) == 0) {
        return cookie.substring(name.length, cookie.length);
        }
    }
    return "";
}

function checkCookie(inputUser) {
    var username = getCookie("username");
    if (username != "") {
        alert("Welcome again " + username);
        //return true;
    } else {
        username = inputUser;
        // username = prompt("Please enter your name:", "");
        if (username != "") { // && username != null
            setCookie("username", username, 365);
        }
        // return false;
    }
}

function deleteCookie() {
    document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}


// window.setCookie = function (cookieName, value, exdays) {
//     var d = new Date();
//     d.setTime(d.getTime() + (exdays*24*60*60*1000));
//     var expires = "expires="+ d.toUTCString();
//     document.cookie = cookieName + "=" + value + ";" + expires + ";path=/";
// };

// window.getCookie = function(cookieName) {
//     var name = cookieName + "=";
//     var decodedCookie = decodeURIComponent(document.cookie);
//     var cookieArray = decodedCookie.split(';');
//     for(var i = 0; i < cookieArray.length; i++) {
//         var cookie = cookieArray[i];
//         while (cookie.charAt(0) == ' ') {
//         cookie = cookie.substring(1);
//         }
//         if (cookie.indexOf(name) == 0) {
//         return cookie.substring(name.length, cookie.length);
//         }
//     }
//     return "";
// };

// window.checkCookie = function() {
//     var username = getCookie("username");
//     if (username != "") {
//     alert("Welcome again " + username);
//     } else {
//         username = prompt("Please enter your name:", "");
//         if (username != "" && username != null) {
//         setCookie("username", username, 365);
//         }
//     }
// };