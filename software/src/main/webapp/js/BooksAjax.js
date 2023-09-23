"use strict";

/**This function is used to send a post request to the server and put a new book to the database
 * All validation checks happen at the server side.
 * */
function BookPost(){

    /**Read form*/
    let form = document.getElementById('BookForm');
    let book_data = new FormData(form);
    const data = {};
    book_data.forEach((value, key) => (data[key] = value));
    var jsonData = JSON.stringify(data);

    /** Create XMLHttpRequest*/
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            book_data = JSON.parse(xhr.responseText);
            $('#tables').html('<h3>Book with ISBN ' + book_data['isbn'] + ' has been added to the database successfully</h3><br>').css('color','black');
        }
        else if (xhr.status !== 200)
            $('#tables').html('Error ' + xhr.status + '. ' + xhr.responseText).css('color','#da0f0f');

    };

    xhr.open('POST','BookActions');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);

}

/**This function is used to send a get request to the server and get all the books of a specific genre*/
function BookGet(){

    /** Create XMLHttpRequest*/
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
           let json = '[';
           json += xhr.responseText;
           json += ']';

            $('#result').html("<h3>List of books with that genre:</h3>").css('color', 'black');
            const responseData = JSON.parse(json);
            let i = 1;
            for (const x in responseData){
                $('#result').append("Book No" + i++);
                $('#result').append(createBookTableFromJSON(responseData[x]));
                $('#result').append("<br>");
            }
            if(!is_guest)
                $("#tables").html('');
            calcPageHeight();
        }
        else if (xhr.status !== 200)
            $('#result').html('Error ' + xhr.status + '. ' + xhr.responseText).css('color','#da0f0f');
    };

    let data = $('#GenreForm').serialize();
    xhr.open('GET','BookActions?'+data);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send();

}

/**This function is used to send a put request to the server and update the number of pages of a book in the database
 * All validation checks happen at the server side.
 * */
function BookPut(){
    /**Read form*/
    let form = document.getElementById('UpdateForm');
    let update_data = new FormData(form);
    const data = {};
    update_data.forEach((value, key) => (data[key] = value));
    var jsonData = JSON.stringify(data);

    /** Create XMLHttpRequest*/
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $('#tables').html('<h3>The No of pages of book with ISBN ' + xhr.responseText + ' has been updated successfully</h3><br>').css('color','black');
        }
        else if (xhr.status !== 200)
            $('#tables').html('Error ' + xhr.status + '. ' + xhr.responseText).css('color','#da0f0f');
    };

    xhr.open('PUT','BookActions');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}

/**This function is used to send a delete request to the server and delete a book from the database*/
function bookDelete(){

    /** Create XMLHttpRequest*/
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $('#tables').html('<h3>Book with ISBN ' + xhr.responseText + ' has been deleted from the database successfully</h3><br>').css('color','black');
        }
        else if (xhr.status !== 200)
            $('#tables').html('Error ' + xhr.status + '. ' + xhr.responseText).css('color','#da0f0f');
    };

    let isbn = $('#isbn2').serialize();
    xhr.open('DELETE','BookActions?'+isbn);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}