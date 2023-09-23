"use strict";

/**This function is used to log the admin in and load the essential content*/
function login(){

    var uname = $('#username').val();
    var pass  = $('#pass').val();

    if(uname !== 'admin' || pass !== 'admin12*')
    {
        $('#msg').html("Wrong credentials").css('color','#da0f0f');
        return;
    }

    $('#welcomemsg').html('Welcome back Mr. Admin :)');
    $('#operations').html('<div id="mynav">' +
        '  <a href="#" onclick="getStudents()">Show students</a>' +
        '  <a href="#" onclick="getLibrarians()">Show librarians</a>' +
        '  <a href="#" onclick="getBookStats()">Book statistics</a>' +
        '  <a href="#" onclick="getGenreStats()">Book statistics per genre</a>' +
        '  <a href="#" onclick="getStudentStatistics()">Student Statistics</a>' +
        '  <a href="#" onclick="RunGiveaway()">Run Monthly Giveaway</a>' +
        '  </div>');

}

/**This function sends a request to the server, to get the names of all the students in the database*/
function getStudents(){

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let json = '[';
            json += xhr.responseText;
            json += ']';
            $('#content').html("<h2>List of Students :</h2>");
            $('#content').append(createAdminTable(json, true));
        }
        else if (xhr.status !== 200)
            alert('Request failed. Returned status of ' + xhr.status);
    };
    xhr.open('GET', 'GetStudents');
    xhr.send();
}

/**This function sends a request to the server, to get the names of all the librarians in the database*/
function getLibrarians(){

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let json = '[';
            json += xhr.responseText;
            json += ']';
            $('#content').html("<h2>List of Librarians :</h2>");
            $('#content').append(createAdminTable(json, false));
        }
        else if (xhr.status !== 200)
            alert('Request failed. Returned status of ' + xhr.status);
    };
    xhr.open('GET', 'GetLibrarians');
    xhr.send();

}

/**This function is used to send a request to the server to delete a student from the database*/
function deleteStudent(row){
    let username = row.value;

    var xhr = new XMLHttpRequest();
    xhr.onload = async function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            getStudents();
            await sleep(200);  //We wait a bit for getStudents() to finish the request so that the message bellow
                                   //doesnt get overwritten
            $('#content').append("<h2>Student with username " + username + " deleted successfully!</h2>");
        } else if (xhr.status !== 200)
            alert('Request failed. Returned status of ' + xhr.status + 'Cannot delete or update a parent row: a foreign key constraint fails');
    };

    xhr.open('DELETE','GetStudents?username='+username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

/**This function is used to send a request to the server to delete a librarian from the database*/
function deleteLibrarian(row){
    let username = row.value;

    var xhr = new XMLHttpRequest();
    xhr.onload = async function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            getLibrarians();
            await sleep(200);  //We wait a bit for getLibrarians() to finish the request so that the message bellow
                                   //doesnt get overwritten
            $('#content').append("<h2>Librarian with username " + username + " deleted successfully!</h2>");
        } else if (xhr.status !== 200)
            alert('Request failed. Returned status of ' + xhr.status + 'Cannot delete or update a parent row: a foreign key constraint fails');
    };

    xhr.open('DELETE','GetLibrarians?username='+username);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

/**This function sends a request to the server, to get the names of all the libraries
 * along with the number of books in them*/
function getBookStats(){

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200)
            drawChart(xhr.responseText, 'Number of books per library', 1);
        else if (xhr.status !== 200)
            alert('Request failed. Returned status of ' + xhr.status);
    };
    xhr.open('GET', 'BooksPerLib');
    xhr.send();
}

/**This function sends a request to the server, to get all the book genres in the database
 * along with the number of books for each genre*/
function getGenreStats(){

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200)
            drawChart(xhr.responseText, 'Number of books per genre',2);
        else if (xhr.status !== 200)
            alert('Request failed. Returned status of ' + xhr.status);
    };
    xhr.open('GET', 'BooksPerGenre');
    xhr.send();
}

/**This function sends a request to the server, to get all the student types
 * along with the number of each type*/
function getStudentStatistics(){

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200)
            drawChart(xhr.responseText, 'Number of students',3);
        else if (xhr.status !== 200)
            alert('Request failed. Returned status of ' + xhr.status);
    };
    xhr.open('GET', 'GetStudentStats');
    xhr.send();
}


function RunGiveaway(){

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200)
            $('#content').html('<h2>'+xhr.responseText+'</h2>');
        else if (xhr.status !== 200)
            alert('Request failed. Returned status of ' + xhr.status + ' Cant run the giveaway, not enough student entries!');
    };
    xhr.open('POST', 'Giveaway');
    xhr.send();

}