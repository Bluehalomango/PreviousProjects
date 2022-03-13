$(document).ready(function() {

    $("body").addClass("js");
  //Sets the slideshow to the first image
  var slideIndex = 0;

  //Automatically starts the slideshow
  showSlides();

  function showSlides() {
    //Finds the slide and dot classes in the html file
    var slides = document.getElementsByClassName("slide");
    var dots = document.getElementsByClassName("dot");

    //Goes through the slides (length 3) and sets the display value of each one to none to hide the image
    for (var i = 0; i < 3; i++) {
      slides[i].style.display = "none";
    }

    //Goes through the dots (length 3) and changes them as the slideshow progresses
    for (var i = 0; i < 3; i++) {
        dots[i].className = dots[i].className.replace(" active", "");
    }


    //Autoincrements the slideimage and sets it to the start when it finishes
    slideIndex++;
    if (slideIndex > 3) {slideIndex = 1}

    //Sets the appropriate slideshow image to display and dot to active
    slides[slideIndex-1].style.display = "block";
    dots[slideIndex-1].className += " active";

    //Loops through every 4 seconds
    setTimeout(showSlides, 4000);
  }

});