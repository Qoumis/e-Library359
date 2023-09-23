/**This js file is from assignment 2.1**/

"use strict";

var is_weak = false, is_matching = false, contains_invalid = false;

/** I want the init function to run after ajax content is fully loaded**/
$(document).ajaxComplete(
    window.onload = function init(){
    document.getElementById("user").selectedIndex = -1;
    document.getElementById("stdtype").selectedIndex = -1;
    $('.student_data').hide();
    $('.lib_data').hide();
    $('#locanswer').hide();
    $('#mapbtn').hide();
    $('#Map').hide();
});

/**This function checks if the passwords are matching and how strong the password is.*/
function checkPass(){
    let p1 = $('#pass').val();
    let p2 = $('#pass1').val();

    if(p1 != p2){
        $('#is_matching').html('Passwords should be matching!').css('color', '#da0f0f');
        is_matching = false;
    }
    else{
        $('#is_matching').html('');
        is_matching = true;
    }

    var digit = [1,2,3,4,5,6,7,8,9,0];
    var special =['!','#','$','%','^','&','*','+','=','?','~','<','>','.',':',';','-'];
    var cnt_di = 0, cnt_sp = 0;
    var has_upper = false, has_lower = false;
    for(let i = 0; i < p1.length; i++){
        for(let j = 0; j < digit.length; j++){
            if(digit[j] == p1.charAt(i)) {          //count the number of digits in the password
                cnt_di++;
                break;
            }
        }
        for(let j = 0; j < special.length; j++){   //count the number of special characters in the password
            if(special[j] == p1.charAt(i)){
                cnt_sp++;
                break;
            }
        }
    }
    if(/[a-z]/.test(p1))     //check for lower case
        has_lower = true;
    if(/[A-Z]/.test(p1))    //check for upper case
        has_upper = true;

    //check for university's name substring
    if(p1.toLowerCase().includes("helmepa") || p1.toLowerCase().includes("tuc") || p1.toLowerCase().includes("uoc"))
        contains_invalid = true;
    else
        contains_invalid = false;

/**print a message accordingly*/
    if(p1.length < 8)
        $('#valid').html('Password too short').css('color','#da0f0f');
    else if(p1.length > 12)
        $('#valid').html('Password too long').css('color','#da0f0f');
    else{
        if(cnt_di >= (p1.length/2)){
            $('#valid').html('Weak password').css('color','#da0f0f');
            is_weak = true;
        }
        else if(has_lower && has_upper && cnt_sp >= 2){
            $('#valid').html('Strong password').css('color','green');
            is_weak = false;
        }
        else if(contains_invalid)
            $('#valid').html('Contains invalid word').css('color','#da0f0f');
        else{
            $('#valid').html('Medium password').css('color','orange');
            is_weak = false;
        }
    }
}

/**This function is used to show/hide the password that the user typed*/
function showPass(){
    let p1 = $('#pass');
    let p2 = $('#pass1');

    if(p1.attr('type') === 'password'){
        p1.attr('type','text');
        p2.attr('type','text');
    }
    else{
        p1.attr('type','password');
        p2.attr('type','password');
    }
}

/**This function prevents the user to sign-in, if the password is weak or if the passwords are not matching
* and pops the according alert
* */
function canSubmit(){
    if(is_weak)
        alert("Your password is weak, change it before signing-in!");
    if(!is_matching)
        alert("Your passwords are not matching!");
    if(contains_invalid)
        alert("Your password contains an invalid word (university's name)");

    /*Stin prwti askisi eixa valei required sto checkbox auto..
   Alla twra zitaei pali na petaei katallilo minima?? Opote prepei na vgei to required??
    */
    let checked = $('#terms').prop('checked');
    if(!checked)
        alert("You need to agree with the terms of use first!");

    let stdnt_correctness = true;
    if($('#user').val() == 'student')
        stdnt_correctness = check_correctness();


   return (is_matching && !is_weak && !contains_invalid && checked && stdnt_correctness);
}

/**this function is used to show/hide student-librarian data accordingly*/
function accType(){
    let type = $('#user').val();

    if(type == 'student'){
        $('.student_data').show();
        $('.lib_data').hide();
        set_required(true, false);
        $("#addrLBL").text("Home Address");
    }
    else{
        $('.student_data').hide();
        $('.lib_data').show();
        set_required(false, true);
        $("#addrLBL").text("Library Address");
    }
    calcPageHeight();
}

/**This function is used to change the required field of some elements based on whether the user is a librarian or a student*/
function set_required(stdnt, lib){
    $('#stdtype').prop('required',stdnt);
    $('#id').prop('required',stdnt);
    $('#start').prop('required',stdnt);
    $('#end').prop('required',stdnt);
    $('#UOC').prop('required',stdnt);
    $('#department').prop('required',stdnt);

    $('#libName').prop('required',lib);
    $('#libInfo').prop('required',lib);
}

/**This function is used to check if the student data is valid and correct*/
function check_correctness(){
    var correct = true;
    let mail = $('#e-mail').val();
    let uni =  document.getElementsByName('university');

    for(let i = 0; i < uni.length; i++){   //find checked radio button value
        if(uni[i].checked) {
            var unival = uni[i].value;
            break;
        }
    }
/**if mail dosnt match university -> permit submit*/
    unival = unival.concat(".gr");
    if(!mail.includes(unival)){
        alert("Your email address does not match your university (should end with: " + unival + ")");
        correct = false;
    }
/**if academic id != 12 digits -> permit submit*/
    let acID = $('#id').val();
    let cnt = 0;
    while(cnt < acID.length)
        cnt++;
    if(cnt != 12){
        alert("That's not a valid academic id (must be 12 digits)");
        correct = false;
    }

    let start = new Date(document.getElementById("start").value);
    let end   = new Date(document.getElementById("end").value);
    if(start >= end){   //Auto einai perito afou meso css den ton afinei na valei megalutero end date apo start date, alla afou to zitouse pali i askisi to evala...
        alert("id start date cannot surpass id end date!");
        correct = false;
    }
    let time = end.getTime() - start.getTime();    //calculate difference in ms
    let days = Math.ceil(time/(1000*3600*24));  //convert to days

    let student = $('#stdtype').val();
    if(student == 'undergraduate' && (days/365) > 6){
        alert("Invalid academic id dates, cannot exceed 6 years");
        correct = false;
    }else if(student == 'postgraduate' && (days/365) > 2){
        alert("Invalid academic id dates, cannot exceed 2 years");
        correct = false;
    }else if(student == 'doctoral' && (days/365) > 5){
        alert("Invalid academic id dates, cannot exceed 5 years");
        correct = false;
    }

    return correct;
}
