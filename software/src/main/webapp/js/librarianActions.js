
/**Shows the form for adding a new book*/
function AddBook(){

    $("#content").html('<h3> Add a new book to the database</h3>');
    $("#content").append("<form id='BookForm'  onsubmit='BookPost(); return false;'>" +
        "    <label for='isbn'>ISBN:</label>" +
        "    <input id='isbn' type='number' name='isbn' required><br>" +
        "    <label for='title'>Book name:</label>" +
        "    <input id='title' type='text' name='title' required><br>" +
        "    <label for='authors'>Authors:</label>" +
        "    <input id='authors' type='text' name='authors' required><br>" +
        "    <label for='genre'>Genre:</label>" +
        "    <input id='genre' name ='genre' type='text' required><br>" +
        "    <label for='url'>Book url-page:</label>" +
        "    <input id='url' type='url' name='url' required pattern='https?://.+' title='Should start with http://'><br>" +
        "    <label for='photo'>Cover photo url:</label>" +
        "    <input id='photo' type='url' name='photo' required pattern='https?://.+' title='Should start with http://'><br>" +
        "    <label for='pages'>No of pages:</label>\n" +
        "    <input id='pages' type='number' name='pages' required><br>" +
        "    <label for='publicationyear'>Publication year:</label>" +
        "    <input id='publicationyear' type='number' name='publicationyear' required><br>" +
        "    <input type='submit' value='Add book'><br>" +
        "</form>")
    $('#tables').html('');

}

/**Shows the field to make a book available*/
function SetAvailable(){

    $("#content").html('<h3> Make a book available to your library</h3>');
    $("#content").append("<label for='isbn'>ISBN:</label>" +
        "<input id='isbn' name='isbn' type='number' required><br>" +
        "<input type='button' value='submit' onclick='AvailabilityPost()'><br>");
    $('#tables').html('');
}

/**This function is used to send a post request to the server make a book available to a specific library
 * (Add a new entry to booksinlibraries)
 * */
function AvailabilityPost(){

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $('#tables').html('<h3>Book with ISBN ' + xhr.responseText + ' has been added to the available books ' +
                'of your library!</h3><br>').css('color','black');
        }
        else if (xhr.status !== 200)
            $('#tables').html('Error ' + xhr.status + '. ' + xhr.responseText).css('color','#da0f0f');
    };

    let isbn = $('#isbn').serialize();
    xhr.open('POST','LibrarianActions?'+isbn);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

/**This function sends a request to the server and gets as a response a json string containing
 * information about all the borrow requests for a library.
 */
function GetRequests(){

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var json = xhr.responseText;

            $('#content').html("<h2>Borrow Requests:</h2>");
            $('#content').append(createRequestsTable(json));
            $('#tables').html('');
            calcPageHeight();
        }
        else if (xhr.status !== 200)
            $('#content').html('Error ' + xhr.status + '. ' + xhr.responseText).css('color','#da0f0f');
    };

    xhr.open('GET','LibrarianActions');
    xhr.send();

}

/**This function is used to send the borrowing_id of a book to the server and update the status of that book*/
function updateStatus(row){
    let borrowing_id = row.value;

    var xhr = new XMLHttpRequest();
    xhr.onload = async function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            GetRequests();
            await sleep(200);

            $('#tables').append("<h2>Status updated successfully</h2>");
        } else if (xhr.status !== 200)
            alert('Request failed. Returned status of ' + xhr.status);
    };

    xhr.open('PUT','LibrarianActions?borrowing_id='+borrowing_id);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}


function getActiveBorrowings(){

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {

            $('#content').html("<h2>Active borrowing in your library:</h2>").css('color', 'black');
            const responseData = JSON.parse(xhr.responseText);
            let i = 1;
            for (const x in responseData){
                $('#content').append("Book No" + i++);
                $('#content').append(createTableFromJSON(responseData[x]));
                $('#content').append("<br>");
            }
            calcPageHeight();
        } else if (xhr.status == 204)
            $('#tables').html('Request failed. Returned status of ' + xhr.status +'. '+ xhr.responseText);
        else
            $('#tables').html('Request failed. Returned status of ' + xhr.status);
    };

    xhr.open('GET','GetBorrowingsPDF');
    xhr.send();

}