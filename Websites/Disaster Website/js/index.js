function initMap(data){
  //Varialbe used for a marker
  var markers;
  //Variable used for lower timeline range
  var timeLow;
  //Varialbe used for upper timeline range
  var timeHigh;
  //Varialbe used for danger rating filter value
  var dangerRating = 0;
  //Varialbe used for category filter boxes selected
  var categories = [];
  //Varialbe used for pin array
  var pins = [];
  //Varialbe used for user's search term
  var searchTerm = "";
  //Varialbe used for disaster array
  var disasterArray = [];
  //Varialbe used for marker pin array
  var markers = [];
  //Varialbe used to set a popup infowindow tab
  var infoWindow = new google.maps.InfoWindow();
  //Varialbe used for timeline slider from HTML page
  var slider = document.getElementById("timeline");
  //Varialbe used to store the disasters that are required from the database
  // (filters unneccessary disaster types)
  var disastersCategories = ["Cyclone", "Hail", "Urban Fire", "Drought",
       "Bushfire", "Heatwave", "Earthquake", "Flood", ]
      
    // Map options
    var options = {
        zoom:4,
        center:{lat:-40.2744,lng: 133.7751}
    }

    //Creates a new map
    var map = new google.maps.Map(document.getElementById('map'), options);  

    //Associate the styled map with the MapTypeId and set it to display.
    // Create a new StyledMapType object, passing it an array of styles,
    // and the name to be displayed on the map type control.
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

    //Sets the styling
    map.mapTypes.set("styled_map", styledMapType);
    map.setMapTypeId("styled_map");

    //Runs the function used to iterate through the records and parse the results
    iterateRecords(data);

    function iterateRecords(data) {
      //This function runs through the disaster database and parses it into an array

        var i = 0;
        $.each(data.result.records, function(recordKey, recordValue) {
            //Goes through each record in the data.gov database and filters out those wanted

            var recordLatitude = recordValue["lat"];
            var recordLongitude = recordValue["lon"];
            var recordTitle = recordValue["title"]; 
            var recordID = recordValue["id"];
            var recordContent = recordValue["description"];
            var recordStart = recordValue["startDate"];

            //Splits to get the year of the disaster
            var recordYear = (recordStart.split(" ")[0].trim()).split("/")[2]

            //Finds the category of disaster
            if (recordValue["title"].includes("Drought")) {
              var recordCategory = "Drought";

            } else if (recordTitle.includes("Heatwave")) {
              var recordCategory = "Heatwave";

            } else if (recordValue["title"].includes(" - ")) {
              var recordCategory = recordValue["title"].split("-")[0].trim();
            } else {
              var recordCategory = recordValue["title"].split(" ")[0].trim();
            }

            //Disaster Rating specifics and statistics
            var recordDeaths = recordValue["Deaths"];
            var recordInjuries = recordValue["Injuries"];
            var recordEvacuated = recordValue["Evacuated"];
            var recordHomeless = recordValue["Homeless"];
            var recordInsurance = recordValue["Insured Cost"];
            var recordTDg = recordValue["Train(s) damaged"];
            var recordTDs = recordValue["Train(s) destroyed"];
            var recordHDg = recordValue["Home(s) damaged"];
            var recordHDs = recordValue["Home(s) destroyed"];
            var recordBDg = recordValue["Building(s) damaged"];
            var recordBDs = recordValue["Building(s) destroyed"];
            var recordMDg = recordValue["Motor Vehicle(s) damaged"];
            var recordMDs = recordValue["Motor Vehicle(s) destroyed"];
            var recordLivestock = recordValue["Livestock destroyed"];

            //Checks that the disaster is in the region of Australia and is included in the defined list of categories to search for
            if(recordLatitude && recordLongitude && disastersCategories.includes(recordCategory) && -10 >= recordLatitude && 100 <= recordLongitude && -50 <= recordLatitude && 160 >= recordLongitude) {

              //Creates numerous categories to calculate the danger rating
              //Each category is based off a distinct statistic or group of statistics and is weighted differently
              //If statements are used to cap categories to a maximum value/percentage

              //First category relates to disaster deaths
              var cat1 = (recordDeaths*0.6);
              if (cat1 > 40) {
                cat1 = 40;
              }

              //Second category relates to casualties (Injuries, homeless, evacuated)
              var cat2 = ((recordEvacuated + recordHomeless + recordInjuries)/100);
              if (cat2 > 25) {
                cat2 = 25;
              }

              //Third category relates to property destroyed (Trains, Houses, Vehicles and Buildings)
              var cat3 = ((recordTDs + recordHDs + recordMDs + recordBDs)/14) 
              if (cat3 > 25) {
                cat3 = 25;
              }

              //Second category relates to property damaged (Trains, Houses, Vehicles and Buildings)
              var cat4 = ((recordBDg + recordTDg + recordHDg + recordMDg)/600)
              if (cat4 > 20) {
                cat4 = 20;
              }

              //Second category relates to livestock killed
              var cat5 = recordLivestock/8000;
              if (cat5 > 15) {
                cat5 = 15;
              }

              //Second category relates to insurance claims and similar expenses
              var cat6 = recordInsurance/20000000;
              if (cat6 > 20) {
                cat6 = 20;
              }

              //Danger rating takes in each category's rating, adding to a base value of 15
              //This base value is used as there are numerous disasters that, while dangerous, have little recored information
              //This also makes the assumption that a disaster shouldn't be classified a disaster if it ranks below 15 on this danger rating
              var DangerRating = 15 + cat1 + cat2 + cat3 + cat4 + cat5 + cat6;

              //Sets the danger rating category according to how dangerous it is
              //This variable is used to select the associated custom icon, with the variable corresponding to the colour
              var rate = "Yellow";
              if (DangerRating > 60) {
                rate = "Red";
              } else if (DangerRating > 30) {
                rate = "Orange";
              }

              //Creates the disaster inside the array with the specific information
              disasterArray[i] = {
                  //Info includes: coordinates, title, description, disaster ID, date, disaster category, year, deaths, injuries, 
                  //trains/houses/vehicles/buildings destroyed or damaged, insurance claim, livestock killed,
                  // amount of people evacuated/homeless/injured, danger rating and custom icon
                    coords:{lat:recordLatitude, lng:recordLongitude},
                    title: recordTitle,
                    content: recordContent,
                    id: recordID,
                    date: recordStart,
                    category: recordCategory,
                    year: recordYear,
                    deaths: recordDeaths,
                    injuries: recordInjuries,
                    trainsDst: recordTDs,
                    trainsDmg: recordTDg,
                    houseDst: recordHDs,
                    houseDmg:recordHDg, 
                    vehDst: recordMDs,
                    vehDmg: recordMDg, 
                    buildDst: recordBDs,
                    buildDmg: recordBDg,
                    insurance: recordInsurance,
                    livestock: recordLivestock,
                    evac: recordEvacuated,
                    homeless: recordHomeless,                
                    rating: DangerRating,
                    iconImage: "images/icons/" + recordCategory + rate + ".png"
                };
                i++;
              }
           });
        
      //Stores dataset in localstorage
      localStorage.setItem("slqData", JSON.stringify(disasterArray));
      //Sets the inital time values
      timeHigh = 2020;
      timeLow = 2000;
      //Runs function to display markers to the map
      MarkersToDisplay();
    }

    function MarkersToDisplay() {
      //Remove all markers in the displaymarkers array
      for (i=0; i < markers.length; i) {
        markers.pop();
      }

      for (i=0; i < disasterArray.length; i++) {
        //Loops through each disaster and checks it against the fitler categories

        if (searchTerm.length != 0) {
          //If a user has entered a search term, it is taken into consideration in the filter
            if (Array.isArray(categories) && categories.length >=1) {
              //if one or more buttons are pressed, then all categories of pressed buttons are shown
                for (j=0; j < categories.length; j++) {
                  //Checks that the disaster year is within the timeframe, part of the selected categories, above or equal to the selected danger rating and includes the search term
                  if ((categories[j] == disasterArray[i].category) && disasterArray[i].year >= timeLow && disasterArray[i].year <= timeHigh 
                  && disasterArray[i].rating >= dangerRating && disasterArray[i].title.includes(searchTerm)) { 
                    //If so, adds the marker to the displaymarker array, signifying that it is valid to be displayed
                    markers.push(disasterArray[i]);
                }
              } 
            }
            else {
              //Else is for no category filter
              //Checks for timeframe, danger rating and search term
              if  (disasterArray[i].year >= timeLow && disasterArray[i].year <= timeHigh 
                && disasterArray[i].rating >= dangerRating && disasterArray[i].title.includes(searchTerm)){
                  //Adds to display
                  markers.push(disasterArray[i]);
              }
            }
        } 
        else {
          //Else is if there is no search term
            if (Array.isArray(categories) && categories.length >=1){
              // if one or more buttons are pressed, then all categories of pressed buttons are shown
              //Checks that the disaster year is within the timeframe
              for (j=0; j < categories.length; j++) {
                //Checks that the disaster year is within the timeframe, part of the selected categories and above or equal to the selected danger rating
                if ((categories[j] == disasterArray[i].category) && disasterArray[i].year >= timeLow 
                && disasterArray[i].rating >= dangerRating && disasterArray[i].year <= timeHigh) { 
                  //Adds to display
                  markers.push(disasterArray[i]);
                }
              } 
            }
            else {
              //Else is for no category filter
              //Checks for timeframe and greater than/equal to danger rating
              if  (disasterArray[i].year >= timeLow && disasterArray[i].year <= timeHigh 
                && disasterArray[i].rating >= dangerRating){
                  //Adds to display
                  markers.push(disasterArray[i]);
              }
            }
          }
      }

      //Removes the pins from the map and then adds the refreshed pin array to the map
      removePins();
      addPins(markers);
    }


    function filter (category) {
    //Function to toggle category selection status
      if (categories.includes(category)) {
        //Removes category from categories array if already present
        var index = categories.indexOf(category);
        categories.splice(index, 1);
      }
      else {
        //Adds category to list of categories to filter
        categories.push(category) 
      }
      MarkersToDisplay()
    }


    function removePins() {
      //Function to remove pins each time the filters are changed, refreshing the map
      //Sets each pin to a null map and then empties the pins array
      for(i=0; i < pins.length; i++){
          pins[i].setMap(null);
      }
      for(i=0; i < pins.length; i++){
          pins[i].setMap(null);
          pins = [];
      }
    }


    function addPins(markers) {
        //Function to loop through displaymarkers array and add each marker to the map
        for(var i = 0; i < markers.length; i++){
            addMarker(markers[i]);
        }
    }
    

    function addMarker(props){
      //The function to add markers to the google map
      //Creates a new marker with set position, google map and animation 
        var marker = new google.maps.Marker({
          position:props.coords,
          map:map,
          animation: google.maps.Animation.DROP
        });
        
        //Checks for and applies custom marker icon
        if(props.iconImage){
          marker.setIcon(props.iconImage);
        }

        //Checks for and then creates the content for the infowindow marker popup
        if(props.title){
          //Adds listener to create popup
          google.maps.event.addListener(marker, 'click', function(){
            //Sets the current disaster ID and category according to the popup
            //As such, only the latest popup's information is set to storage
            localStorage.setItem('current ID', props.id);
            localStorage.setItem('category', props.category);
            //Adds content and styling to the popup infowindow
            infoWindow.setContent ("<h3>" + props.title + "</h3>" +
            "<p style='margin-bottom: 2%'>" + props.date + "</p>" +
            '<p class ="shortener">' + props.content + "..." + "</p>" +
            '<button class="button" onclick="toInformation()">Learn More</button>'); 
            infoWindow.open(map, marker);
          });
        }
        //Adds this marker to the pins array for later use
        pins.push(marker);
    }


    slider.oninput = function() {
      //This event is run when the user interacts with the timeline
      //It changes the time range according to the user's selection
      var mid = parseInt(this.value);
      timeLow = mid - 10;
      timeHigh = mid + 10;
      //Redisplays markers according to new timeline range
      MarkersToDisplay();
    }

    $("#danger-rating").bind('keyup input', function(){
      //Changes the value of the danger rating when the user interacts with the input
      dangerRating = parseInt(this.value);
    
      //If the parsed danger rating is greater than 0, than it is a valid input
      if (dangerRating >= 0) {
        //Redisplays markers according to new danger rating
        MarkersToDisplay();
      } else {
        //This else is reached if the input is invalid (negative, not an integer, etc)
        //It sets a predefined integer value of 0, the lowest rating
        dangerRating = 0;
        //Redisplays markers according to new danger rating
        MarkersToDisplay();
      }
    });

    $("#form-filter-search").keyup (function() {
      //Takes the user's search term and runs it through the disasters
      searchTerm = $(this).val();
      MarkersToDisplay();
    });


    //Button functionality for the filter categories

    //Cyclone button, toggles Cyclone filter
      $("#cyclone").click(function() {
        filter("Cyclone");
        $("#cyclone").toggleClass("button-selected")
      });

    //Hail button, toggles Hail filter
      $("#hail").click(function() {
        filter("Hail");
        $("#hail").toggleClass("button-selected")
      });
      
    //Earthquake button, toggles Earthquake filter
      $("#earthquake").click(function() {
        filter("Earthquake");
        $("#earthquake").toggleClass("button-selected")
      });
        
    //Flood button, toggles Flood filter
      $("#flood").click(function() {
        filter("Flood");
        $("#flood").toggleClass("button-selected")
      });
        
    //Urban Fire button, toggles Urban Fire filter
    $("#urban-fire").click(function() {
      filter("Urban Fire");
      $("#urban-fire").toggleClass("button-selected")
    });

    //Drought button, toggles Drought filter
    $("#drought").click(function() {
      filter("Drought");
      $("#drought").toggleClass("button-selected")
    });

    //Heatwave button, toggles Heatwave filter
    $("#heatwave").click(function() {
      filter("Heatwave");
      $("#heatwave").toggleClass("button-selected")
    });

    //Bushfire button, toggles Bushfire filter
    $("#bushfire").click(function() {
      filter("Bushfire");
      $("#bushfire").toggleClass("button-selected");
    });
}

function toInformation(){
  //Sets the URL to the specific category version of the information page.
  window.location.href = "information.php?" + "&type=" + localStorage.getItem("category");
}

function Hide(HideID) {  
  //Styles the instructional popup
  $(".pop-up").fadeOut();
}



$(document).ready(function() {

	var data = {
    resource_id: 'ad5c6594-571e-4874-994c-a9f964d789df',
    limit: 900
  }

	$.ajax({
		url: 'https://data.gov.au/data/api/3/action/datastore_search',
		data: data,
		dataType: "jsonp", // We use "jsonp" to ensure AJAX works correctly
		cache: true,
		success: function(data) {
      //Initalises the map once the data is retrieved
			initMap(data);
		}
	});

});