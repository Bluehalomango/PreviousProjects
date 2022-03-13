$(document).ready(function() {

  $(".book").click(function (e) {   
    //Adds event listener to the book class on the current page

    //Obtains the value associated with the clicked button and stores it
    sessionStorage.setItem('info', $(this).val())

    //Gives a location reference for the button to allow it to act as a link
    location.href='book.html';
  });
  

  if ($('#package').length > 0) {
    //Checks if the page has the package id
    //If so, goes through various checks to add appropriate content

    //Sets the local variable purchase to the stored value from the previous button
    let purchase = sessionStorage.getItem('info');

    //Goes through various checks to determine which package the user is purchasing

    //If the purchase value is equal to 'luxury'
    if (purchase == 'luxury'){
      //Sets the purchase variable to the content to be added to the booking page
      //This is a section class with an image and text relevant to the package the user is purchasing
      purchase = "<section class = 'book_item'><img src='images/accom/hotel.jpg' class='book_img'><p class='book_text'>Luxury hotel package</p></section>"
    }    

    if (purchase == 'history'){
      purchase = "<section class = 'book_item'><img src='images/accom/refurb.jpg' class='book_img'><p class='book_text'>Historical hotel package</p></section>"
    }  
    
    if (purchase == 'culture'){
      purchase = "<section class = 'book_item'><img src='images/accom/local.jpg' class='book_img'><p class='book_text'>Cultural hotel package</p></section>"
    }  

    if (purchase == 'rustic'){
      purchase = "<section class = 'book_item'><img src='images/accom/rural.jpg' class='book_img'><p class='book_text'>Rustic hotel package</p></section>"
    }   

    if (purchase == 'exfil'){
      purchase = "<section class = 'book_item'><img src='images/rooms/exfil.jpg' class='book_img'><p class='book_text'>Exfil Escape Room package</p></section>"
    }

    if (purchase == 'meltdown'){
      purchase = "<section class = 'book_item'><img src='images/rooms/meltdown.jpg' class='book_img'><p class='book_text'>Meltdown Escape Room package</p></section>"
    }

    if (purchase == 'survival'){
      purchase = "<section class = 'book_item'><img src='images/rooms/survival.jpg' class='book_img'><p class='book_text'>Survival Escape Room package</p></section>"
    }

    if (purchase == 'winter'){
      purchase = "<section class = 'book_item'><img src='images/rooms/winter.jpg' class='book_img'><p class='book_text'>Winter Escape Room package</p></section>"
    }

    if (purchase == 'bunker'){
      purchase = "<section class = 'book_item'><img src='images/rooms/bunker.jpg' class='book_img'><p class='book_text'>Bunker Escape Room package</p></section>"
    }

    if (purchase == 'escape'){
      purchase = "<section class = 'book_item'><img src='images/rooms/escape.jpg' class='book_img'><p class='book_text'>Escape style Escape Room package</p></section>"
    }   

    //Adds the formatted html to the package class, which is only found in the booking page
    //This is specialised for each package (accommodations/escape room) that the user can book
    //The idea is to give the user information regarding their purchase so they know what they are buying
    document.getElementById("package").innerHTML = purchase;

  }
})


