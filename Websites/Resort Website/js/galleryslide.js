
//This code is not within a document ready tag as the webpage has a large amount of images, thus heavily affecting the load time

function zoom(imgs) {
      //Function to zoom in on the image

      //Gets the location of where the zoomed image will be displayed 
      var zoom = document.getElementById("galleryzoom");

      //Sets the source location of the image being zoomed as the source of the zoom
      //Because zoom was already set to the appropriate ID in the document,
      //this places the clicked image into that location (galleryzoom)
      zoom.src = imgs.src;
      
      //Gets the location of where the images alt text will be displayed 
      var text = document.getElementById("gallerytext");

      //Gets the alt text of the image to include as a caption for the image
      //Added as a html text element
      text.innerHTML = imgs.alt;
    }

//Sets the slides to the images that are in the 'gallery_column' classes
var slides = document.getElementsByClassName("gallery_column");

//Goes through the set of images when the page is opened and orders them with a for loop
for (i = 0; i < slides.length; i++) {
  slides[i].style.order = i;
  //Each image is given the order associated with its placement in the html structure
}

//Function used to control left (previous) movement  
function leftslide() {
  var i;
  //Goes through a for loop that goes for the amount of images set as slides (slide length)
  for (i = 0; i < slides.length; i++) {
    //Checks if the current slide is at the end position
    if ( slides[i].style.order == slides.length - 1 ) {
      //Resets the end slide to the starting position
      slides[i].style.order = 0
    }
    else {
      //Increments the slide order to move the slides along
      slides[i].style.order ++;
    }
  }
}
  
//Function used to control right (next) movement  
function rightslide() {
  var i;
  //Goes through a for loop that goes for the amount of images set as slides (slide length)
    for (i = 0; i < slides.length; i++) {
      //Checks if the current slide is at the start position
      if ( slides[i].style.order == 0) {
        //Resets the starting slide to the end position
        slides[i].style.order = slides.length - 1
      }
      else {
        //Decreases the slide order to move the slides along backwards
        slides[i].style.order --;
    }
  }
}