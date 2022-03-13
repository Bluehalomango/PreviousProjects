function initMap(data){
  //Function used to initalise the map on the info page

    function mapCreate(latitude, long, marker) {
      //Creates the map with the latitude and longitude of the disaster

      var options = {
        //Sets a number of unique options for the info map
        //This includes greater zoom, no zoom, scale street view, rotate or similar controls/UI
          zoom:5,
          center:{lat:latitude,lng: long},
          gestureHandling: 'none',
          zoomControl: false,
          mapTypeControl: false,
          scaleControl: false,
          streetViewControl: false,
          rotateControl: false,
          fullscreenControl: false
      }

      //Creates the new map for the HTML element
      var map = new google.maps.Map(document.getElementById('map'), options);  
  
      //Styles the map
      const styledMapType = new google.maps.StyledMapType(
        [
          {
            "elementType": "geometry",
            "stylers": [
              {
                "color": "#f5f5f5"
              }
            ]
          },
          {
            "elementType": "labels.icon",
            "stylers": [
              {
                "visibility": "off"
              }
            ]
          },
          {
            "elementType": "labels.text.fill",
            "stylers": [
              {
                "color": "#616161"
              }
            ]
          },
          {
            "elementType": "labels.text.stroke",
            "stylers": [
              {
                "color": "#f5f5f5"
              }
            ]
          },
          {
            "featureType": "administrative",
            "elementType": "labels.text",
            "stylers": [
              {
                "color": "#f7f7f7"
              },
              {
                "weight": 0.5
              }
            ]
          },
          {
            "featureType": "administrative.land_parcel",
            "elementType": "labels.text.fill",
            "stylers": [
              {
                "color": "#bdbdbd"
              }
            ]
          },
          {
            "featureType": "landscape",
            "elementType": "geometry",
            "stylers": [
              {
                "color": "#003670"
              }
            ]
          },
          {
            "featureType": "poi",
            "elementType": "geometry",
            "stylers": [
              {
                "color": "#003670"
              }
            ]
          },
          {
            "featureType": "poi",
            "elementType": "labels.text.fill",
            "stylers": [
              {
                "color": "#757575"
              }
            ]
          },
          {
            "featureType": "poi.park",
            "elementType": "geometry",
            "stylers": [
              {
                "color": "#003670"
              }
            ]
          },
          {
            "featureType": "poi.park",
            "elementType": "labels.text.fill",
            "stylers": [
              {
                "color": "#9e9e9e"
              }
            ]
          },
          {
            "featureType": "road",
            "elementType": "geometry",
            "stylers": [
              {
                "color": "#ffffff"
              }
            ]
          },
          {
            "featureType": "road.arterial",
            "elementType": "labels.text.fill",
            "stylers": [
              {
                "color": "#757575"
              }
            ]
          },
          {
            "featureType": "road.highway",
            "elementType": "geometry",
            "stylers": [
              {
                "color": "#dadada"
              }
            ]
          },
          {
            "featureType": "road.highway",
            "elementType": "labels.text.fill",
            "stylers": [
              {
                "color": "#616161"
              }
            ]
          },
          {
            "featureType": "road.local",
            "elementType": "labels.text.fill",
            "stylers": [
              {
                "color": "#9e9e9e"
              }
            ]
          },
          {
            "featureType": "transit.line",
            "elementType": "geometry",
            "stylers": [
              {
                "color": "#e5e5e5"
              }
            ]
          },
          {
            "featureType": "transit.station",
            "elementType": "geometry",
            "stylers": [
              {
                "color": "#eeeeee"
              }
            ]
          },
          {
            "featureType": "water",
            "elementType": "geometry",
            "stylers": [
              {
                "color": "#0e1626"
              }
            ]
          },
          {
            "featureType": "water",
            "elementType": "labels.text.fill",
            "stylers": [
              {
                "color": "#9e9e9e"
              }
            ]
          }
        ],
        { name: "Styled Map" }
      );
  
    //Applies style
    map.mapTypes.set("styled_map", styledMapType);
    map.setMapTypeId("styled_map");

    //Runs the function to add the neccessary marker
    addMarker (marker);

    function addMarker(props){
      //Creates google map marker with position, map, animation and custom icon
      var marker = new google.maps.Marker({
        position:props.coords,
        map:map,
        animation: google.maps.Animation.DROP,
      });
      //If a custom icon exists, uses it
      if(props.iconImage){
        marker.setIcon(props.iconImage);
        }
      }
    }

  function iterateRecords(data) {
    //This function runs through the disaster array and finds the correct disaster
    var i = 0;
    for (i; i < data.length; i++) {
      var current_item = data[i];
      if (current_item.id == localStorage.getItem('current ID')) {
          //Once it has found the disaster in the array corresponding to disaster selected on the map, uses that info
            populateMap(current_item);
      }
    }     
  }

  function populateMap (marker) {
    //Uses the disaster's coordinates to create the map with, then adds the marker to the map
    var latitude = marker.coords.lat;
    var longitude = marker.coords.lng;
    mapCreate (latitude, longitude, marker);
  }

  iterateRecords (data);
}
  
$(document).ready(function() {
  //Ajax is used to load the functions once the google map API has responded
  $.ajax({
    success: function(data) {
      //Parses the disaster array from local storage
      var slqData = JSON.parse(localStorage.getItem("slqData"));
      initMap(slqData);
    }
  });
});