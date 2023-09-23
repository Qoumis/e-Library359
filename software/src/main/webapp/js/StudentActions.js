var stdnt_lat, stdnt_lon;

function showBrowse(){
    $("#content").load("guestBooks.html");
    $("#content").css('width','700px');
    $('#tables').html('');
}

/**This function sends a request to the server and gets all the reviews of a book in the database*/
function getReviews(row){

    let isbn = row.value;

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            $("#tables").html("<div id='curr' style='margin-top: 65%'></div><br>");
            $("#curr").html("<h2>Here are all the reviews for book with ISBN " +isbn+ ": </h2>");

            let i = 1;
            for (const x in responseData){          //create a table for each review
                $('#curr').append("Review No" + i++);
                $('#curr').append(createTableFromJSON(responseData[x]));
                $('#curr').append("<br>");
            }
            calcPageHeight();
        }
        else if (xhr.status == 204)
            $("#tables").html("<h2 style='margin-top: 65%'>There are no reviews for this book yet!</h2>");
        else
            $('#tables').html("<h2 style='margin-top: 65%; color: red'>Unknown Error. Server returned status "+xhr.status+"</h2>");
    };

    xhr.open('GET','GetReviews?isbn='+isbn);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

/**This function sends a request to the server and gets all the libraries (that hava the chosen book available)
 * along with their info
 */
function getLibraries(row){
    let isbn = row.value;
    getStudentLocation();

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            $("#tables").html("<div id='curr' style='margin-top: 65%'></div><br>");
            $("#curr").html("<h2>Calculating Distance for each library, please wait... </h2>");

            calculateDistances(responseData);  //prints happen here after distance calculation is done

            calcPageHeight();
        }
        else if (xhr.status == 204)
            $("#tables").html("<h2 style='margin-top: 65%'>This book is not currently available in a library.</h2>");
        else
            $('#tables').html("<h2 style='margin-top: 65%; color: red'>Unknown Error. Server returned status "+xhr.status+"</h2>");
    };

    xhr.open('GET','GetLibraries?isbn='+isbn);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

/**This function is used to get student's lat and lon from the database*/
function getStudentLocation(){

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            const responseData = JSON.parse(xhr.responseText);
            stdnt_lat = responseData['lat'];
            stdnt_lon = responseData['lon'];
        }
        else if (xhr.status !== 200)
            alert("Failed to get student's location got return status of " + xhr.status);
    };

    xhr.open('GET','GetLocation');
    xhr.send();
}

/**This function is used when a student asks to borrow a books. (A request is sent to the server to create a new entry
 * in borrowing table and make all the necessary changes to other tables as well)
 */
function BorrowBook(row){
    var bookId = row.value;

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $("#tables").html("<h2 style='margin-top: 65%'>Borrow Request created successfully! " +
                "Now you have to wait for the librarian to confirm it.</h2>");
        }
        else if (xhr.status !== 200)
            alert("Unknown error occurred, got status code of " + xhr.status);
    };

    xhr.open('POST','BorrowActions?bookcopy_id='+bookId);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function ReturnBook(row){
    var borrowing_id = row.value;

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            getStudentBorrowings();
            $("#tables").html('<h2>Return request created successfully</h2>');
            checkForBorrowingEnd();
        }
        else if (xhr.status !== 200)
            alert("Unknown error occurred, got status code of " + xhr.status)
    };

    xhr.open('PUT','BorrowActions?borrowing_id='+borrowing_id);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();

}

/**This function is used to show the review form to the student*/
function WriteReview(row){
   let isbn = row.value;

   $("#tables").html('<h2>Write your review</h2>' +
       '<form id="reviewForm" onsubmit="ReviewPost('+isbn+'); return false">' +
       '    <label for="score">Book rating score</label>' +
       '    <select id="score" name="reviewScore" required>' +
       '        <option value="1">1 - The Worst</option>' +
       '        <option value="2">2 - Bad</option>' +
       '        <option value="3" selected>3 - Neutral</option>' +
       '        <option value="4">4 - Good</option>' +
       '        <option value="5">5 - Excellent</option>' +
       '    </select><br><br><br>' +
       '    <label for="reviewText">Add a description :</label><br>' +
       '    <textarea id="reviewText" name="reviewText" rows="20" cols="60"></textarea><br><br><br>' +
       '    <input type="submit" value="Upload review">' +
       '</form>');
   calcPageHeight();
}
 /**This function is used to send the student's review to the server*/
function ReviewPost(isbn) {
     var xhr = new XMLHttpRequest();
     xhr.onload = function () {
         if (xhr.readyState === 4 && xhr.status === 200) {
             $("#tables").html('<h2>Thank you for submitting your review. You got +1 entry to the monthly giveaway!</h2>')
         } else if (xhr.status !== 200)
             alert("Unknown error occurred, got status code of " + xhr.status)
     };

     var data = $('#reviewForm').serialize();
     xhr.open('POST', 'PostReview?' + data + '&isbn=' + isbn);
     xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
     xhr.send();
 }

/**This function sends a request to the server and gets information about student's past and active borrowings*/
function getStudentBorrowings(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            if(xhr.responseText.length <= 2)
                $('#content').html("<h2>You dont have any borrowings yet!</h2>");
            else{
                $('#content').html("<h2>Here is a list with all of your borrowings <br>(past and present) :</h2>").css('color', 'black');
                const responseData = JSON.parse(xhr.responseText);
                let i = 1;
                for (const x in responseData){
                    $('#content').append("Borrowing No" + i++);
                    $('#content').append(createBorrowTable(responseData[x]));
                    $('#content').append("<br>");
                }
                $("#tables").html('');
            }
            calcPageHeight();
        }
        else
            $('#tables').html('Request failed. Returned status of ' + xhr.status + xhr.responseText);
    };

    xhr.open('GET','BorrowActions');
    xhr.send();
}

/**This function is run when a student logs in and sends a request to the server to check if there
 * are any books due to return in the next days.
 */
function checkForBorrowingEnd(){
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            $("#reminder").addClass("mycontainer");
            $("#reminder").html("<h2 style='color: red'>"+xhr.responseText+"</h2>");
        }
        else if (xhr.status == 204){
            $("#reminder").removeClass("mycontainer");
            $("#reminder").html('');
        }
    };

    xhr.open('GET','CheckBorrowings');
    xhr.send();

}