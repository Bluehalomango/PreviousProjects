function init (data) {
    //Starts the search function to initalise the page
    searchRecords(data);

    function searchRecords(data) {
        //This function runs through the disaster array and finds the correct disaster
        var i = 0;
        for (i; i < data.length; i++) {
            var current_item = data[i];
            if (current_item.id == localStorage.getItem('current ID')) {
                //Once it has found the disaster in the array corresponding to disaster selected on the map, uses that info
                populatePage(current_item);
            }
        }      
    }

    function populatePage(info) {
        //Assigns the disaster description, category, date, title and rating to be displayed and adds them to the HTML
        var infoContent = info.content;
        var infoCategory = info.category;
        var infoDate = info.date;
        var infoTitle = info.title;
        var infoRating = info.rating;
        document.getElementById("rating").innerHTML = "Danger Rating: " + infoRating.toFixed(0);
        document.getElementById("disasterTitle").innerHTML = infoTitle;
        //A number of checks that break up the description if it is above a certain character length
        //When obtained from the data.gov database, the description is in a single paragraph.
        //As such, this breaks it up into more user friendly segments.
        if (infoContent.length > 1400) {
            var insertIndex = infoContent.indexOf(". ", 1000);
            infoContent = infoContent.slice(0, insertIndex + 1) + "<br>"+ "<br>" + infoContent.slice(insertIndex + 1);
            if (infoContent.length > 2400) {
                var insertIndex = infoContent.indexOf(". ", 2000);
                infoContent = infoContent.slice(0, insertIndex + 1) + "<br>" + "<br>" + infoContent.slice(insertIndex + 1);
                if (infoContent.length > 3400) {
                    var insertIndex = infoContent.indexOf(". ", 3000);
                    infoContent = infoContent.slice(0, insertIndex + 1) + "<br>" + "<br>" + infoContent.slice(insertIndex + 1);
                    if (infoContent.length > 4400) {
                        var insertIndex = infoContent.indexOf(". ", 4000);
                        infoContent = infoContent.slice(0, insertIndex + 1) + "<br>" + "<br>" + infoContent.slice(insertIndex + 1);
                    }
                }
            }
        }
        document.getElementById("disasterContent").innerHTML = infoContent;
        //Goes through the statistics and adds those that are present (not null) to the page
        var stats = "Date of Disaster: " + infoDate + "<br>" + "Type of Disaster: " + infoCategory + "<br>";
        if (info.deaths != null) {
            stats += "Deaths: " + info.deaths + "<br>";
        }

        if (info.injuries != null) {
            stats += "Injuries: " + info.injuries + "<br>";
        }
        if (info.evac != null) {
            stats += "Evacuated: " + info.evac + "<br>";
        }
        if (info.homeless != null) {
            stats += "Homeless: " + info.homeless + "<br>";
        }

        if (info.insurance != null) {
            stats += "Insurance Costs: $" + info.insurance + "<br>";
        }
        if (info.livestock != null) {
            stats += "Livestock Killed: " + info.livestock + "<br>";
        }

        if (info.trainsDst != null) {
            stats += "Trains Destroyed: " + info.trainsDst + "<br>";
        }
        if (info.trainsDmg != null) {
            stats += "Trains Damaged: " + info.trainsDmg + "<br>";
        }

        if (info.houseDst != null) {
            stats += "Houses Destroyed: " + info.houseDst + "<br>";
        }
        if (info.houseDmg != null) {
            stats += "Houses Damaged: " + info.houseDmg + "<br>";
        }

        if (info.vehDst != null) {
            stats += "Vehicles Destroyed: " + info.vehDst + "<br>";
        }
        if (info.vehDmg != null) {
            stats += "Vehicles Damaged: " + info.vehDmg + "<br>";
        }

        if (info.buildDst != null) {
            stats += "Buildings Destroyed: " + info.buildDst + "<br>";
        }
        if (info.buildDmg != null) {
            stats += "Buildings Damaged: " + info.buildDmg + "<br>";
        }

        document.getElementById("disasterStats").innerHTML = stats;
        //Once all the disaster info has been added to the page, adds the safety info
        setInfo(infoCategory);
    }

    function setInfo(infoCategory) {
        //This function adds the safety info associated with the disaster category
        document.getElementById("category").innerHTML = passedArray[0];
        document.getElementById("info").innerHTML = passedArray[1];
        document.getElementById("effects").innerHTML = passedArray[2];
        document.getElementById("actions").innerHTML = passedArray[3];

        //Creates a list to add the dot points to
        ul = document.createElement('ul');
        document.getElementById('dotPoints').appendChild(ul);
        //Starts the list at element 4, which corresponds to the first dot point
        var j = 4;
        //As the amount of dot points differs for each category, the loop only reads non-null values (non="NIL")
        for (j; j <passedArray.length;j++) {
            if (passedArray[j] != "NIL") {
                //If there is a valid value, adds to the dot points on the page
                let li = document.createElement('li');
                ul.appendChild(li);
                li.innerHTML = passedArray[j];
            }
        }
            
        //Creates the link to the game page for the disaster category
        var link = document.getElementById('gameLink');
        link.href = "game.html?disaster=" + infoCategory;
        //Sets game ID and score count to default, sets category to local storage
        localStorage.setItem('gameID', 1);
        localStorage.setItem('count', 0);
        localStorage.setItem('category', infoCategory);
    }
}


function readMoreDescription() {
    //this function expands the general information on the page
    var readMoreText = document.getElementById("readMore");
    var readLessDescriptionText = document.getElementById("readLessDescription");
    var disasterContent = document.getElementById("disasterContent");
  
    //Changes the button that is to be seen
    readMoreText.style.visibility = "hidden";
    readLessDescriptionText.style.visibility = "visible";

    //Expands the hidden text
    disasterContent.style.whiteSpace = "normal";
    disasterContent.style.overflow = "visible";
  }

  function readLessDescription() {
    //This function collapses the general information on the page
    var readMoreText = document.getElementById("readMore");
    var readLessDescriptionText = document.getElementById("readLessDescription");
    var disasterContent = document.getElementById("disasterContent");
  
    //Changes the button that is to be seen
    readMoreText.style.visibility = "visible";
    readLessDescriptionText.style.visibility = "hidden";

    //Hides the text
    disasterContent.style.whiteSpace = "nowrap";
    disasterContent.style.overflow = "hidden";
  }



  function readMoreSafety() {
    //This function expands the safety information on the page
    var moreText = document.getElementById("more");
    var moreText2 = document.getElementById("more2");
    var readLessText = document.getElementById("readLess");
    var readMoreSafetyText = document.getElementById("readMoreSafetyInfo");

    //Changes the button that is to be seen
    readLessText.style.visibility = "visible";
    readMoreSafetyText.style.visibility = "hidden";

    //Displays all the information
    moreText.style.display = "inline";
    moreText2.style.display = "inline";
  }

  function readLessSafety() {
    //This function collapses the safety information on the page
    var moreText = document.getElementById("more");
    var moreText2 = document.getElementById("more2");
    var readLessText = document.getElementById("readLess");
    var readMoreSafetyText = document.getElementById("readMoreSafetyInfo");  
  
    //Changes the button that is to be seen
    readLessText.style.visibility = "hidden";
    readMoreSafetyText.style.visibility = "visible";

    //Displays only the general safety information
    moreText.style.display = "inline";
    moreText2.style.display = "none";
    
  }


$(document).ready(function() {
    //Sets the current CYOA game story according to the information obtained from our database
    localStorage.setItem('game', JSON.stringify(passedStory));
    //Parses the disaster array from local storage
    var slqData = JSON.parse(localStorage.getItem("slqData"));

    if (!slqData) {
        //If no data was found, automatically defaults to disaster ID 1
        localStorage.setItem('current ID', 1);
    }
    init(slqData);

});
