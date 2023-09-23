google.charts.load("current", {packages:["corechart"]});

/**This function calculates the page's height each time we dynamically load content
 * so that the footer doesn't overlap with the content
 */
function calcPageHeight(){

    var contHeight   = Math.max(document.getElementById('tables').offsetHeight, document.getElementById('content').offsetHeight);
    var footerHeight = document.getElementById('footer').offsetHeight;
    var minHeight = contHeight+footerHeight;

    document.getElementById('maincont').style.paddingBottom = minHeight + 'px';
}


/**This function is used to create a table with all the data of a student in the DB*/
function createTableFromJSON(data) {
    var html = "<table><tr><th>Category</th><th>Value</th></tr>";

    for (const x in data) {
        var category = x;
        if(category == "has_won" || category == "borrow_count")
            continue;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";
    return html;
}

/**This function is used to create a table with the names of all the sudents/librarians in the DB
 * @param flag if true then the function is being called from students else it's for librarians
 * */
function createAdminTable(data, flag) {
    var html = "<table><tr><th>Username</th><th>First Name</th><th>Last Name</th></tr>";

    const responseData = JSON.parse(data);
    for (const x in responseData){
        html += "<tr>";
        for (const y in responseData[x]) {
            var value = responseData[x][y];
            html+= "<td>" + value + "</td>";
        }
        if(flag)
            html+= "<td> <button value="+responseData[x]['username']+" onclick='deleteStudent(this)' style='color: red'> Delete</button></td>"
        else
            html+= "<td> <button value="+responseData[x]['username']+" onclick='deleteLibrarian(this)' style='color: red'> Delete</button></td>"
        html += "</tr>";

    }
    html += "</table>";
    return html;
}

function createBookTableFromJSON(data) {
    var html = "<table><tr><th>Category</th><th>Value</th></tr>";

    for (const x in data) {
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    if(!is_guest) {
        html += "<tr><td>User reviews</td><td><button value=" + data['isbn'] + " onclick='getReviews(this)' style='color: forestgreen'> Show reviews</button> </td></tr>";
        html += "<tr><td>Book availability</td><td><button value=" + data['isbn'] + " onclick='getLibraries(this)' style='color: deepskyblue'> Show available libraries</button> </td></tr>";
    }
    html += "</table>";
    return html;
}

/**This function is used to create a table with all the library's requests (past and present). If the request needs
 * confirmation there is a button for the librarian to confirm it.
 */
function createRequestsTable(data){
    var html = "<table><tr><th>ISBN</th><th>student's First Name</th><th>student's Last Name</th><th>email</th><th>Status</th></tr>";

    const responseData = JSON.parse(data);
    for (const x in responseData){
        html += "<tr>";
        for (const y in responseData[x]) {
            if(y == "borrowing_id" || y == "status")
                continue;
            var value = responseData[x][y];
            html+= "<td>" + value + "</td>";
        }
        html+= "<td>" + responseData[x]['status'] + "</td>";
        if(responseData[x]['status'] == 'requested' || responseData[x]['status'] == 'returned')
            html+= "<td> <button value="+responseData[x]['borrowing_id']+" onclick='updateStatus(this)' style='color: blue'> Update</button></td>"
        html += "</tr>";
    }
    html += "</table>";
    return html;
}

/**This function is used to create a table with all the data of a library that has the chosen book available*/
function createAvailableLibTable(data) {
    var html = "<table><tr><th style='background-color: #912f2b'>Category</th><th style='background-color: #912f2b'>Value</th></tr>";

    for (const x in data) {
        if (x == "bookcopy_id" || x == "lat" || x == "lon")
            continue;
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "<tr><td>Borrow from this library</td><td><button value=" + data['bookcopy_id'] + " onclick='BorrowBook(this)' style='color: blue'> Borrow now</button></td></tr>";
    html += "</table>";
    return html;
}

/**This function is used to create a table with all the data of a book that has been borrowed by the student*/
function createBorrowTable(data){
    var html = "<table><tr><th>Category</th><th>Value</th></tr>";

    for (const x in data) {
        if (x == "ISBN" || x == "borrowing_id")
            continue;
        var category = x;
        var value = data[x];
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    if(data['Status'] == "borrowed")
        html += "<tr><td>Return this book</td><td><button value=" + data['borrowing_id'] + " onclick='ReturnBook(this)' style='color: blue'> Return </button></td></tr>";
    else if(data['Status'] == "successEnd")
        html += "<tr><td>Write a review</td><td><button value=" + data['ISBN'] + " onclick='WriteReview(this)' style='color: blue'> Review now </button></td></tr>";
    html += "</table>";
    return html;
}

/**This function uses TrueWay Matrix API and calculates the distance (and time to get there by car) between a student's
 * house and all the libraries that have available the book they selected.
 * After the distances are calculated we add 2 new fields to each library table (distance and duration).
 * After that we call here the function that prints the tables to our html page
 */
function calculateDistances(libs_data){
    var destinations = "";

    for(const x in libs_data){
        var lat = libs_data[x]['lat'];
        var lon = libs_data[x]['lon'];
        destinations += lat + "%2C"+ lon + "%3B";
    }

    const data = null;
    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            const response = JSON.parse(this.responseText);

            /**Add time and distance field to each library object*/
            for(const i in response.distances[0]) {
               libs_data[i]['Distance from your address'] = (response.distances[0][i]/1000).toFixed(1) + " km";
                libs_data[i]['Minutes to get there'] = Math.round(response.durations[0][i]/60);
            }
            /**Short tables by time to get there with car*/
            libs_data.sort((a, b) => {
                if (a['Minutes to get there'] < b['Minutes to get there']) {
                    return -1;
                }
                if (a['Minutes to get there'] > b['Minutes to get there']) {
                    return 1;
                }
                return 0;
            });

            /**Print the tables*/
            $("#curr").html("<h2>Here are all the available libraries for the selected book : </h2>");
            let i = 1;
            for (const x in libs_data){              //create a table for each available library
                $('#curr').append("Library No" + i++);
                $('#curr').append(createAvailableLibTable(libs_data[x]));
                $('#curr').append("<br>");
            }
        }
    });

    xhr.open("GET", "https://trueway-matrix.p.rapidapi.com/CalculateDrivingMatrix?origins="+stdnt_lat+"%2C"+stdnt_lon+"&destinations="+destinations);
    xhr.setRequestHeader("X-RapidAPI-Key", "0502338363mshfb57ba43f0a5136p10759bjsna800af5849ff");
    xhr.setRequestHeader("X-RapidAPI-Host", "trueway-matrix.p.rapidapi.com");

    xhr.send(data);
}

/**This function is used to pause program execution for ms*/
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

/**This function is used to draw a pie chart with Google chart tools
 * @param flag we use this to make a different array, depending on who's calling the function
 * */
function drawChart(json, title, flag) {
    const responseData = JSON.parse(json);
    var array = new Array();
    array.push(['lib', 'Number']);
    for (const x in responseData){
        if(flag == 1)
            array.push([responseData[x]["libraryname"], parseInt(responseData[x]["NoBooks"])]);
        else if(flag == 2)
            array.push([responseData[x]["genre"], parseInt(responseData[x]["NoBooks"])]);
        else
            array.push([responseData[x]["student_type"], parseInt(responseData[x]["Number"])]);
    }

    var data = google.visualization.arrayToDataTable(array);

    var options = {
        title: title,
        is3D: true,
        sliceVisibilityThreshold: 0,
    };

    var chart = new google.visualization.PieChart(document.getElementById('pie'));
    chart.draw(data, options);
}

/**This function is used to create a form with the student's current data
 * User can edit some fields and hit update button to send changes back to the server
 */
function createStudentForm(data){
    var html = '<form id="updateForm" onSubmit="UpdateStudentData(); return false">';
    for (const x in data) {
        var category = x;
        var value = data[x];
        if(category == "has_won" || category == "borrow_count")
            continue;

        /**In this case data will be editable*/
        if(category != 'university' && category != 'department' && category != 'student_type' && category != 'student_id_from_date'
            && category != 'student_id_to_date' && category != 'username' && category != 'email')
        {
            html += "<label htmlFor="+ category +">"+ category+ ":</label>";
            html += "<input type='text' id='"+category+"' name='" + category +"' value='"+value+"'><br><br>";
        }
        /**In this case data is readonly (username, email, etc)*/
        else
        {
            html += "<label htmlFor="+ category +">"+ category+ ":</label>";
            html += "<input type='text' id='"+category+"' name='" + category +"' value='"+value+"' readonly><br><br>";
        }

    }
    html += '<input type="submit" value="Update">';
    html += "<span id='error'></span><br><br>";
    html += '</form>';
    return html;
}

/**Same as the function above but for librarians*/
function createLibrarianForm(data){
    var html = '<form id="updateForm" onSubmit="UpdateLibrarianData(); return false">';
    for (const x in data) {
        var category = x;
        var value = data[x];

        /**In this case data will be editable*/
        if(category != 'username' && category != 'email')
        {
            html += "<label htmlFor="+ category +">"+ category+ ":</label>";
            html += "<input type='text' id='"+category+"' name='" + category +"' value='"+value+"'><br><br>";
        }
        /**In this case data is readonly (username, email)*/
        else
        {
            html += "<label htmlFor="+ category +">"+ category+ ":</label>";
            html += "<input type='text' id='"+category+"' name='" + category +"' value='"+value+"' readonly><br><br>";
        }

    }
    html += '<input type="submit" value="Update">';
    html += "<span id='error'></span><br><br>";
    html += '</form>';
    return html;
}



function showLogin() {
    $("#content").load("login.html");
    $("#tables").html('');
}

async function showRegistrationForm() {
    $("#content").load("registration.html");
    $("#tables").html('');
    await sleep(300);
    calcPageHeight();
}