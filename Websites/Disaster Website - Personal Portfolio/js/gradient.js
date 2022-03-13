//The red green blue variables associated with the colour gradient function.
var r = 255;
var g = 0;
var b = 0;
var r2 = 255;
var g2 = 255;
var b2 = 0;
var endColour;
var clockWise = true;
var flip = false;

//Function that changes the text for the landing page after button press
function gradientSetter(item) {
    item.innerHTML = "You pressed the button.";
    document.getElementById("title").style.fontSize = "4em";
    document.getElementById("title").style.opacity = "1";
    document.getElementById("title").style.fontFamily = "Oswald";
    //Sets the interval to update the gradient
    setInterval(gradient, 50);
  }
  
  //Gradient function for the landing page background
  function gradient() {
    //Creates the canvas background element and sets the necessary properties
    var canvas = document.createElement('canvas'),        
    canvasBack = canvas.getContext('2d'),
    cw = canvas.width = window.innerWidth;
    ch = canvas.height = window.innerHeight;
  
    //Sets the red and blue variables to create the blue gradient change
    r = 0;
    r2 = 0;
    b = 255;
    b2 = 255;
  
    //If the green value is less than max and hasn't flipped yet, increase base green and decrease secondary green
    if (g < 255 && !flip) {
      g = increase(g);
      g2 = decrease(g2);
    } else if (g == 255 && !flip) {   //If the green value is max and hasn't flipped yet, set flip to true
      flip = true;
    } else if (g > 0 && flip) {   //If the green value is less than max and has flipped yet, decrease base green and increase secondary green
      g = decrease(g);
      g2 = increase(g2);
    } else {    //This else statements tick over when base green reaches 0. It flips the colours back to count up again.
      flip = false;
    }
  
    /*    Code snippet for rainbow gradient instead of blue (I originally wrote this and then changed it to blue afterwards)
      if (r == 255 && b == 0 && g < 255) {
        g = increase(g);
        r2 = decrease(r2);
      } else if (r == 255 && b > 0) {
        g2 = increase(g2);
        b = decrease(b);
      } else if (g == 255 && r == 0 && b < 255) {
        b = increase(b);
        g2 = decrease(g2);
      } else if (g == 255 && r > 0) {
        b2 = increase(b2);
        r = decrease(r);
      } else if (b == 255 && g == 0 && r < 255) {
        r = increase(r);
        b2 = decrease(b2);
      } else if (b == 255 && g > 0) {
        r2 = increase(r2);
        g = decrease(g);
      }
    */
  
    //Creates the liner gradient for the canvas
    var grad = canvasBack.createLinearGradient(0, 0, cw, 0);
    //Adds the two colours to create the gradient between
    grad.addColorStop(0, 'rgb(' + r + ', ' + g + ', ' + b + ')');
    grad.addColorStop(1, 'rgb(' + r2 + ', ' + g2 + ', ' + b2 + ')');
  
    /*
    if (clockWise) {
      grd.addColorStop(0, 'rgb(' + r + ', ' + g + ', ' + b + ')');
      grd.addColorStop(1, 'rgb(' + r2 + ', ' + g2 + ', ' + b2 + ')');
  
    } else {
      grd.addColorStop(0, 'rgb(' + r2 + ', ' + g2 + ', ' + b2 + ')');
      grd.addColorStop(1, 'rgb(' + r + ', ' + g + ', ' + b + ')');
  
    }
    */
  
    //Sets the fill and size of the canvas using the gradient
    canvasBack.fillStyle = grad;
    canvasBack.fillRect(0, 0, cw, ch);
    document.getElementById("titleCard").style.background = 'url(' + canvas.toDataURL() + ')'; //Updates the background url to the canvas
  }
  
  //Basic function to increase by 5
  function increase (up) {
    up += 5;
    return up;
  }
  
  //Basic function to decrease by 5
  function decrease (down) {
    down -= 5;
    return down;
  }