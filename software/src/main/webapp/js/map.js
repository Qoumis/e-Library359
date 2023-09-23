/**This file is from assignment 2.2**/

"use strict";

var lat = -1, lon = -1;

/**This function is used to get the address that the user entered*/
function get_location(){
    var country = $('#country').val();
    var city    = $('#city').val();
    var addr    = $('#addr').val();
    var addrno  = $('#addrNumber').val();

     var total = addr.concat(" ", addrno, " ", city, " ", country);

    search_Geocode(total);
}

/**This function is used to hide the map when user enters a different location*/
function revalidate(){
    document.getElementById("Map").innerHTML = "";  //clear map (if it exists)
    $('#Map').hide();
    $('#mapbtn').hide();
    $('#locanswer').html("You need to validate the location first, to see it on the map.").css('color', 'orange');
    $('#locanswer').show();
}

/**This function checks if the address is a valid address within crete and saves the lat and lon if it is.
 * If not it prints a message accordingly.
 */
function search_Geocode(addr){

    const data = null;
    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === this.DONE) {
            if(this.responseText.length <= 2) {  //if response is empty -> print message and do nothing
                $('#locanswer').html("This address does not match an existing location!").css('color', '#da0f0f');
                $('#locanswer').show();
                $('#mapbtn').hide();
            }
             else{
                const json = JSON.parse(this.responseText);
                var addressDetails=json[0];

                lat = addressDetails['lat']; //now in ass3 we need to save lat and lon even if location is not in crete
                lon = addressDetails['lon']; //to save it on the DB anyway...

                if(!addressDetails['display_name'].includes('Crete')){ //if location is out of crete -> print message and do nothing
                    $('#locanswer').html("The service is currently only available in regions within Crete.").css('color','orange');
                    $('#locanswer').show();
                    $('#mapbtn').hide();
                    return;
                }

                $('#locanswer').hide();
                $('#mapbtn').show();
                console.log(lon + "   " + lat);

            }
        }
    });

    xhr.open("GET", "https://forward-reverse-geocoding.p.rapidapi.com/v1/search?q="+addr+"&accept-language=en&polygon_threshold=0.0");
    xhr.setRequestHeader("X-RapidAPI-Host", "forward-reverse-geocoding.p.rapidapi.com");
    xhr.setRequestHeader("X-RapidAPI-Key", "0502338363mshfb57ba43f0a5136p10759bjsna800af5849ff");

    xhr.send(data);
}

function show_map(){
    $('#Map').show();
    /**init map*/
    var map = new OpenLayers.Map("Map");
    var mapnik         = new OpenLayers.Layer.OSM();
    map.addLayer(mapnik);

    /**init layer for the marker*/
    var marLayer = new OpenLayers.Layer.Markers( "Marker" );
    map.addLayer(marLayer);

    /**set position on map*/
    var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
    var toProjection   = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
    var position       = new OpenLayers.LonLat(lon, lat).transform( fromProjection, toProjection);
    /**set marker*/
    var myMarker = new OpenLayers.Marker(position);
    marLayer.addMarker(myMarker);

    /**set center position*/
    const zoom = 12;
    map.setCenter(position, zoom);
}