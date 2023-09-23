"use strict";

var uname_ok = true , email_ok = true , id_ok = true;

/**This function is used to check if username already exists in database*/
function UnameCheck(){

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) { //all good remove msg (if it exists) and set cont_value to true
            $('#uname_msg').html('');
            uname_ok = true;
        } else if (xhr.status !== 200) {                  //error wait for user to make changes
            $('#uname_msg').html("A user with that username already exists").css('color','#da0f0f');
            uname_ok = false;
        }
    };
    var data = $('#regForm').serialize();
    xhr.open('GET', 'CheckUname?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

/**This function is used to check if username already exists in database*/
function emailCheck(){

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) { //all good remove msg (if it exists) and set cont_value to true
            $('#email_msg').html('');
            email_ok = true;
        } else if (xhr.status !== 200) {                  //error wait for user to make changes
            $('#email_msg').html("A user with that email already exists").css('color','#da0f0f');
            email_ok = false;
        }
    };
    var data = $('#regForm').serialize();
    xhr.open('GET', 'CheckEmail?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

/**This function is used to check if academic id already exists in database*/
function IdCheck(){

    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) { //all good remove msg (if it exists) and set cont_value to true
            $('#id_msg').html('');
            id_ok = true;
        } else if (xhr.status !== 200) {                  //error wait for user to make changes
            $('#id_msg').html("A user with that academic id already exists").css('color','#da0f0f');
            id_ok = false;
        }
    };
    var data = $('#regForm').serialize();
    xhr.open('GET', 'CheckID?'+data);
    xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function RegisterPOST(){

    if(!canSubmit())    //Wait until user corrects all the errors (weak password etc.)
        return;

    if(!uname_ok || !email_ok || !id_ok){
        $('#msg').html('Correct the errors indicated above to continue!').css('color','#da0f0f');;
        return;
    }

    /**Read form*/
    let form = document.getElementById('regForm');
    let reg_data = new FormData(form);
    const data = {};
    reg_data.forEach((value, key) => (data[key] = value));

    /**Add extra fields*/
    data["lat"] = lat;      //Gia na parei ta swsta lat kai lon prepei na exei patisei to koumpi validate location o xristis
    data["lon"] = lon;      //TO DO: mporei pio meta na ton anagkazw na to pataei gia na sinexisei
    let address =  $('#addr').val();
    let addrNum = $('#addrNumber').val();
    address = address + " " + addrNum;
    data["address"] = address;
    /**convert to json*/
    var jsonData = JSON.stringify(data);

    /** Create XMLHttpRequest*/
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {  //on success update page content
            const responsetext = JSON.parse( xhr.responseText);
            $('#content').html("");
            $('#content').append("<h2>Registration completed successfully! </h2>");
            $('#content').append("<h3>Here's all the date you gave : </h3>");
            $('#content').append(TableFromJSON_ignore_empty(responsetext));
            $('#content').append("<h2>Continue by logging-in</h2>");
            $('#content').append("<span><button onclick='showLogin()'>Continue</button></span>");
        } else if (xhr.status !== 200) {
            $('#msg').html('Unknown error occurred, server returned status of ' + xhr.status);
        }
    };
    /**send Post request to Register servlet*/
    let user_type = $('#user').val();
    if(user_type == "student")
        xhr.open('POST','RegisterStudent');
    else
        xhr.open('POST','RegisterLibrarian');
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(jsonData);
}

/**This function is used to create a table with all the data that the user submitted*/
function TableFromJSON_ignore_empty(data) {
    var html = "<table><tr><th>Category</th><th>Value</th></tr>";

    delete data['terms_of_use'];
    for (const x in data) {
        var category = x;
        var value = data[x];
        if(data[x] !="")
            html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";
    return html;
}