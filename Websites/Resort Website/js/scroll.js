$(document).ready(function() {

  $("#localnav a").on('click', function(event) {
  //Function to move the user to the appropriate section in the about us page
    
    //Cancels default behaviour
    event.preventDefault();

    //Obtains the hash from the current instance
    //I.e. Obtains the designated location from the clicked button
    var target = $(this.hash);

      $('html, body').animate({
        //Function to provide the smooth scroll animation
        scrollTop: $(target).offset().top
      }, 1200, function(){
      });
  });


//Sets previous scroll position to window offset
var previous = window.pageYOffset;

window.onscroll = function() {
//Function to hide the navigation bar when scroll along the about us page

  //Sets current offset to new scroll position
  var current = window.pageYOffset;

  //Checks if scrolling up
  if (previous > current) {
    //Sets the website navigation bar to default position and the local nav bar to below it
    document.getElementById("aboutnav").style.top = "0";
    document.getElementById("localnav").style.top = "70px";
  } 
  //Checks if scrolling down
  else {
    //Hides the website navigation bar and sets the local nav bar to the top of the page
    document.getElementById("aboutnav").style.top = "-100px";
    document.getElementById("localnav").style.top = "0px";
  }
  previous = current;
}



});