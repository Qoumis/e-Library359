"use strict";

/**This function is used to send student's credentials to the server and log them in if the student exists*/
function loginStudent() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $("#welcomemsg").html("Welcome " + xhr.responseText + "!");
            $('#content').html('');
            enableStudentOptions();
            is_guest = false;
        }
        else if (xhr.status !== 200)
            $("#msg").html(xhr.responseText).css('color','red');

    };
    var data = $('#logForm').serialize();
    xhr.open('POST', 'LogInStudent?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

/**Same as the student function above*/
function loginLibrarian() {
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $("#welcomemsg").html("Welcome " + xhr.responseText + "!");
            $('#content').html('');
            enableLibrarianOptions();
        }
        else if (xhr.status !== 200)
            $("#msg").html(xhr.responseText).css('color','red');

    };
    var data = $('#logForm').serialize();
    xhr.open('POST', 'LogInLibrarian?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

/**This function is used to send a request to the server to invalidate the session*/
function logout(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200){
            $('#welcomemsg').html('Welcome to E-library 359');
            $("#mynav").html('');
            $('#content').load("HomePage.html");
            $("#tables").html("<h2>Or you can always browse the available books</h2>");
            $("#tables").append("<div id='browse'></div>");
            $("#browse").html("<button onclick='enableBrowse()'>Browse Books</button>");

            is_guest = true;
            $("#reminder").removeClass("mycontainer");
            $("#reminder").html('');
            $("#giveaway").removeClass("mycontainer");
            $("#giveaway").html('');
        }
        else if (xhr.status !== 200)
            alert('Request failed. Returned status of ' + xhr.status);
    };
    xhr.open('DELETE', 'LogOut');
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function LoadLBform(){
    $("#content").load("LBlogin.html");
}