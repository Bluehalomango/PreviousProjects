var down;      //Keeps track of direction of the scroll
var previousScroll = 0;     //Keeps track of the previous scroll position to ascertain scroll direction
var scrollPositions = [0];  //A dynamic array that stores the document positions of each section for the specific screen size
var scrollIDs = ["#titleCard", "#bio", "#explore", "#partA", "#partB", "#partC", "#reflect", "#portfolio", "#portfolio"];   //List to store the different sections IDs of the page

//Function to set the scroll positions dynamically for the page
function setPositions () {
  //Gets the sections on the page and the user's window height
  var sect = document.getElementsByTagName("section");
  var height = window.innerHeight;
  //Goes through each section on the page
  for (var i = 1; i < sect.length + 1;i++) {
    //Adds the sections location relative to the user's window height to the position array
    scrollPositions.push((height * i));
  }
  //Adds a final position that compensates for padding
  scrollPositions.push((sect.length + 1) * height + 50);
}

//Function to add smooth scroll to the scroll event
window.addEventListener('wheel', function(event) {
  currentScroll = document.documentElement.scrollTop;   //Gets the current scroll position relative to the page
  allowed = true;       //Variable to control delayed animations
  //If the previous animation is still in progress, prevents the next animation from occuring
  if ($("html, body").is(':animated')) {
    allowed = false;
  }
  //While the function is allowed to progress (a previous animation is not in progress)
  while (allowed) {
    //Checks the position of the scroll event to determine direction
    if (event.deltaY < 0) {
      //User is scrolling up, reduce scroll position to account for this
      currentScroll-=10;
      down = false;
    }
    if (event.deltaY > 0) {
      //User is scrolling down, increase scroll position to account for this
      currentScroll+= 10;
      down = true;
    }
    //Loops through the scroll position array
    for (var i = 0; i < scrollPositions.length;i++) {
      //Checks that scroll position is within the pages boundaries (First and last scroll position)
      if (scrollPositions[i] <= currentScroll && currentScroll < scrollPositions[i+1]) {
        //If motion has been tagged as downward, animates the scroll to go to the next scroll position
        if (down) {
          $("html, body").animate( 
            { scrollTop: $(scrollIDs[i + 1]).offset().top }, 800); 
        } else {
          //If motion has been tagged as upward, animates the scroll to go to the previous scroll position
          $("html, body").animate( 
            { scrollTop: $(scrollIDs[i - 1]).offset().top }, 800); 
        }
        //Once the loop has found the appropriate position to scroll to, break the loop
        break;
      }
    }
    allowed = false;    //Sets allowed to false to show that the animation is still occuring
  }
});



$(document).ready(function() {
  //Sets the scroll positions once the page has loaded
  setPositions();
});

