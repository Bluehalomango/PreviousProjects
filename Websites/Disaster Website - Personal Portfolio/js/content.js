//Collection of variables for use in innerHTML code for the different section button text.
var bioInfo = ["Student", "Team leader", "Human"];
var exploreInfo = ["Developed The Idea", "Found The Database", "Presented My Pitch"];
var partAInfo = ["Described The Design Concept", "Defined The Scope", "Drafted The Completion Plan"];
var partBInfo = ["Engineered The Database", "Coded The Timeline", "Formulated The Danger Rating"];
var partCInfo = ["Coded The 'Adventure' Game", "Created The Graphics", "Polished The JavaScript"];
var reflectInfo = ["My Expectations", "What I Have Learnt", "What Could Be Changed"];
//Tracker variables that keep tabs on what part of each section the user is in
var bioInfoTracker = 1;
var exploreInfoTracker = 1;
var partAInfoTracker = 1;
var partBInfoTracker = 1;
var partCInfoTracker = 1;
var reflectInfoTracker = 1;

//Function to change the text and display elements of the bio section
function bio(item) {
  //Changes the text of the section's button
  item.innerHTML = bioInfo[bioInfoTracker];
  //Runs the function to change the content of the section
  changeContent("#bio", bioInfoTracker);
  bioInfoTracker++;
  //Resets back to the start of the list
  if (bioInfoTracker == bioInfo.length) {
    bioInfoTracker = 0;
  }
}

//Function to change the text and display elements of the Design Exploration section
function explore(item) {
  item.innerHTML = exploreInfo[exploreInfoTracker];
  changeContent("#explore", exploreInfoTracker);
  exploreInfoTracker++;
  if (exploreInfoTracker == exploreInfo.length) {
    exploreInfoTracker = 0;
  }
}

//Function to change the text and display elements of the Part A section
function partA(item) {
  item.innerHTML = partAInfo[partAInfoTracker];
  changeContent("#partA", partAInfoTracker);
  partAInfoTracker++;
  if (partAInfoTracker == partAInfo.length) {
    partAInfoTracker = 0;
  }
}

//Function to change the text and display elements of the Part B section
function partB(item) {
  item.innerHTML = partBInfo[partBInfoTracker];
  changeContent("#partB", partBInfoTracker);
  partBInfoTracker++;
  if (partBInfoTracker == partBInfo.length) {
    partBInfoTracker = 0;
  }
}

//Function to change the text and display elements of the Part C section
function partC(item) {
  item.innerHTML = partCInfo[partCInfoTracker];
  changeContent("#partC", partCInfoTracker);
  partCInfoTracker++;
  if (partCInfoTracker == partCInfo.length) {
    partCInfoTracker = 0;
  }
}

//Function to change the text and display elements of the Reflection section
function reflect(item) {
  item.innerHTML = reflectInfo[reflectInfoTracker];
  changeContent("#reflect", reflectInfoTracker);
  reflectInfoTracker++;
  if (reflectInfoTracker == reflectInfo.length) {
    reflectInfoTracker = 0;
  }
}


//Main function to change the displayed content of each section 
function changeContent(section, child) {
  //Runs the clear function to reset the section
  hideAll(section);   
  //Child increments are used to account for the other child elements in the section
  //Sections with images have more child elements (the images) and as such make use of more increments
  //If statement for sections where images are on the LHS
  if (section == "#explore") {
    child++;
    //Gets the required image to be displayed
    var query = " img:nth-child(" + child + ")";
    //Creates the query with the image and associated section
    var q1 = section + query;
    //Sets the visibility of the image
    setVisibilty(q1, "block");
    child+=2;
  } 
  //If statement for sections where images are on the RHS
  if (section == "#partC") {
    child+=2;
    var query = " img:nth-child(" + child + ")";
    var q1 = section + query;
    setVisibilty(q1, "block");
    child++;
  }
  //If statement to account for the single image in the bio section
  if (section == "#bio") {
    child++;
  }
  //Increments to account for heading
  child+=2;
  //Creates the query for the necessary article element in the section
  var query = " article:nth-child(" + child + ")";
  var q1 = section + query;
  //Sets the visibility of the article section
  setVisibilty(q1, "block");
}

//Function to set the display visiblity of a given object to a given property
function setVisibilty(object, property) {
  var object = document.querySelector(object).style.display = property;
}

//Function to first hide all the elements of a section before displaying the correct information
function hideAll (section) {
  //If statements are used to account for image displacement (bio) or to hide the images 
  if (section == "#explore") {
    for (var i = 1; i < 4; i++) {
      //Gives the set visibility the corresponding query and display property
      setVisibilty((section + " img:nth-child(" + i + ")"), "none");
    }
    for (var i = 5; i < 8; i++) {
      setVisibilty((section + " article:nth-child(" + i + ")"), "none");
    }
  } else if (section == "#partC") {
    for (var i = 2; i < 5; i++) {
      setVisibilty((section + " img:nth-child(" + i + ")"), "none");
    }
    for (var i = 5; i < 8; i++) {
      setVisibilty((section + " article:nth-child(" + i + ")"), "none");
    }
  } else if (section == "#bio") {
    for (var i = 3; i < 6; i++) {
      setVisibilty((section + " article:nth-child(" + i + ")"), "none");
    }
  } else {
    for (var i = 2; i < 5; i++) {
      setVisibilty((section + " article:nth-child(" + i + ")"), "none");
    }
  }
}

//Function to loop through the article elements of the page and set their font sizes
function loopFonts () {
  var articles = document.querySelectorAll("article");
  //Loop for all article elements
  for (var i = 0; i < articles.length;i++) {
    //Displays the article to obtain its exact size
    if  (articles[i].style.display == "block") {
      var size = articles[i].getBoundingClientRect().height;
    } else {
      //For articles that were not already displayed, displays them to obtain height and then hides them afterwards
      articles[i].style.display = "block";
      var size = articles[i].getBoundingClientRect().height;
      articles[i].style.display = "none";
    }
    //Sets the font size and line height proportionately to the size of the text
    articles[i].style.fontSize = setFonts(size);
    articles[i].style.lineHeight = setFonts(size);
  }
}

//Function to set font size for a given article size
function setFonts(size) {
  //If there is little text in the article, increases font size
  if (size < 150) {
    return ("2em");
  } else if (size < 250) {
    return ("1.6em");
  } else if (size < 400) {
    return ("1.4em");
  } else {
    //This font size is for the largest articles, allowing the text to fit properly
    return ("1.2em");
  }
}

$(document).ready(function() {
  //Sets the font sizes once the page has loaded
  loopFonts();

  //Code to add the image popup for the appropriate classes
  //Code used is
  $('.imgEnlarge').magnificPopup({
    type: 'image',    //Sets type of popup
    mainClass: 'mfp-with-zoom', //This is for CSS animation below
    image: {
			verticalFit: true,  
      titleSrc: 'alt'   //Sets the caption text of the popup to the alt tag for the image element
    },
    zoom: {
      enabled: true, //Enables zoom popup animation affect
      duration: 300, //Duration of the effect, in milliseconds
      easing: 'ease-in-out', //CSS transition easing function
    }
  });
  
//Adds functionality to the local nav bar
$("#nav a").on('click', function(event) {
  //Function to move the user to the appropriate section on the page
    event.preventDefault();       //Cancels default behaviour
    var target = $(this.hash);    //Obtains the hash location from the clicked button
    $('html, body').animate({
      //Function to provide the smooth scroll animation
      scrollTop: $(target).offset().top
    }, 1300, function(){
    });
  });
});

