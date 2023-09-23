"use strict";

var student_data, librarian_data;
var is_guest = true;

/** run this after page is loaded*/
$(document).ready(isLoggedIn);

/**This function is used to check if the user's already logged-in on a previous session*/
function isLoggedIn(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {  //if user is already logged-in load their content
            var response = JSON.parse(xhr.responseText);
            $("#welcomemsg").html("Welcome back " + response["loggedIn"] + "!");
            $('#content').html('');
            if(response["user_type"] == "student")
                enableStudentOptions();
            else
                enableLibrarianOptions();
            is_guest = false;
        }
        else if (xhr.status !== 200){                 //else load the starting page
            $("#content").load("HomePage.html");
            $("#tables").html("<h2>Or you can always browse the available books</h2>");
            $("#tables").append("<div id='browse'></div>");
            $("#browse").html("<button onclick='enableBrowse()'>Browse Books</button>");
            is_guest = true;
        }
        calcPageHeight();
    };
    xhr.open('GET', 'isLoggedIn');
    xhr.send();
}

/**This function is used to show the options for a logged in student*/
function enableStudentOptions(){

    $('#mynav').html("<a onclick=showStudentData()>Show your data</a>");
    $('#mynav').append("<a onclick='showBrowse()'>Browse Books</a>");
    $('#mynav').append("<a onclick='getStudentBorrowings()'>View Borrowings</a>");
    $('#mynav').append("<a onclick='logout()'>Logout</a>");

    checkForBorrowingEnd();
    showGiveawayResults();
}

/**This function is used to show the options for a logged in librarian*/
function enableLibrarianOptions(){
    $('#mynav').html("<a onclick=showLibrarianData()>Show your data</a>");
    $('#mynav').append("<a onclick='AddBook()'>Add new Book</a>");
    $('#mynav').append("<a onclick='SetAvailable()'>Book Availability</a>");
    $('#mynav').append("<a onclick='GetRequests()'>View borrow requests</a>");
    $('#mynav').append("<a onclick='getActiveBorrowings()'>View Active borrowings</a>");
    $('#mynav').append("<a onclick='logout()'>Logout</a>");
}

/**This function is for guests. Shows the form for searching books*/
function enableBrowse(){
    $("#browse").load("guestBooks.html");

}

/**This function is used to get student's data from the server's database*/
function showStudentData(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {

            student_data = JSON.parse(xhr.responseText);
            delete student_data['user_id'];
            delete student_data['student_id'];

            $('#tables').html("<h1>Your Data</h1>");
            $('#tables').append(createTableFromJSON(student_data));
            $('#tables').append("<br> <button onclick='EditStudentData()'>Edit your data</button>");
            $('#content').html('');
            calcPageHeight();
        }
        else if (xhr.status !== 200)
            alert('Request failed. Returned status of ' + xhr.status);

    };

    xhr.open('GET', 'StudentData');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

/**This function is used to get librarian's data from the server's database*/
function showLibrarianData(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {

            librarian_data = JSON.parse(xhr.responseText);
            delete librarian_data['library_id'];

            $('#tables').html("<h1>Your Data</h1>");
            $('#tables').append(createTableFromJSON(librarian_data));
            $('#tables').append("<br> <button onclick='EditLibrarianData()'>Edit your data</button>");
            $('#content').html('');
            calcPageHeight();
        }
        else if (xhr.status !== 200)
            alert('Request failed. Returned status of ' + xhr.status);

    };

    xhr.open('GET', 'LibrarianData');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function EditStudentData(){

    $('#tables').html("<h2>Edit your data</h2>");
    $('#tables').append(createStudentForm(student_data));
    calcPageHeight();

}

function EditLibrarianData(){
    $('#tables').html("<h2>Edit your data</h2>");
    $('#tables').append(createLibrarianForm(librarian_data));
    calcPageHeight();
}

/**This function is used to send the student's updated
 * data back to the server and save it to the DataBase
 */
function UpdateStudentData(){
    /**Read form*/
    let form = document.getElementById('updateForm');
    let reg_data = new FormData(form);
    const data = {};
    reg_data.forEach((value, key) => (data[key] = value));
    var jsonData = JSON.stringify(data);

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {       //All good update page

            student_data = JSON.parse(xhr.responseText);
            $('#tables').html("<h1>Your updated data</h1>");
            $('#tables').append(createTableFromJSON(student_data));
            $('#tables').append("<br> <button onclick='EditStudentData()'>Edit your data</button>");
            calcPageHeight();
        }
        else if (xhr.status !== 200) {                        //Wait for user to correct input errors
            $('#error').html("Correct all input errors to continue!").css('color','#da0f0f');
        }

    };

    xhr.open('PUT', 'StudentData');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}

/**This function is used to send the librarian's updated
 * data back to the server and save it to the DataBase
 */
function UpdateLibrarianData(){
    /**Read form*/
    let form = document.getElementById('updateForm');
    let reg_data = new FormData(form);
    const data = {};
    reg_data.forEach((value, key) => (data[key] = value));
    var jsonData = JSON.stringify(data);

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {       //All good update page

            librarian_data = JSON.parse(xhr.responseText);
            $('#tables').html("<h1>Your updated data</h1>");
            $('#tables').append(createTableFromJSON(librarian_data));
            $('#tables').append("<br> <button onclick='EditLibrarianData()'>Edit your data</button>");
            calcPageHeight();
        }
        else if (xhr.status !== 200) {                        //Wait for user to correct input errors
            $('#error').html("Correct all input errors to continue!").css('color','#da0f0f');
        }
    };

    xhr.open('PUT', 'LibrarianData');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}


/**This function is used to get the data of all the books in the DB*/
function getBooks(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let json = '[';
            json += xhr.responseText;
            json += ']';

            $('#tables').html("<h2>List of available books :</h2>");
            const responseData = JSON.parse(json);
            let i = 1;
            for (const x in responseData){
                $('#tables').append("Book No" + i++);
                $('#tables').append(createBookTableFromJSON(responseData[x]));
                $('#tables').append("<br>");
            }
            calcPageHeight();
        }
        else if (xhr.status !== 200)
            alert('Request failed. Returned status of ' + xhr.status);

    };

    xhr.open('GET', 'GetBookList');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send();
}

function showGiveawayResults(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $("#giveaway").addClass("mycontainer");
            $("#giveaway").html("<h2 style='color: #003a70'>"+xhr.responseText+"</h2>");
        }
        else if (xhr.status == 204){
            $("#giveaway").removeClass("mycontainer");
            $("#giveaway").html('');
        }
    };

    xhr.open('GET', 'Giveaway');
    xhr.send();

}