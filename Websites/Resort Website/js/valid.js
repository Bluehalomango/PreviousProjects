$(document).ready(function() {
//These functions are used for form validation
//Only one function will be completely commented, as they are both similar
   
    $("#contact form").submit(function(event) {
      //Calls function for contact form submit button

        //Gets the values from the approriate form fields
        var Name = $("#fname").val();
        var Language = $("#language").val();
        var Email = $("#email").val();
        var message = $("#message").val();

        //Sets complete status to false
		    var complete = false;

      //Checks if the fields have any entries in them
      //If so, sets complete to true to allow form submission
		  if(Name != "" && Language != "" && Email != "" && message != "") {
		  	complete = true;
		  }

      //Individual checks to add error classes to the forms that don't have any values
		  if( Name == "") {
			  $("#fname").addClass("error").closest("label").addClass("error");
      }
        
      if(Language == "") {
			  $("#language").addClass("error").closest("label").addClass("error");
      }
        
      if(Email == "") {
			  $("#email").addClass("error").closest("label").addClass("error");
      }

      if(message == "") {
			  $("#message").addClass("error").closest("label").addClass("error");
      }
      
      //Checks if the forms have been filled and are complete
		  if(complete) {
        //Toggles the submission buttons text and disables it
			  $("#contact form button").html("Thank You!").attr('disabled', true);
      }
        
      //If forms are not filled, toggles the submission button to alert user that the input data is invalid
		  else {
			  $("#contact form button").html("Invalid");
		  }

      //Prevents default behaviour
		  event.preventDefault();

    });
    
    //Removes error class from the appropriate forms when values are entered
    $("#fname, #language, #email, #message").keydown(function() {
		$(this).removeClass("error").closest("label").removeClass("error");
	});



    $("#booking form").submit(function(event) {
      //Calls function for booking form confirm button
      //Sets variables to values from the forms
      //Checks if the variables are empty
      //If so, add error classes and prevent submission until changed

      var Name = $("#fname").val();
      var Address = $("#address").val();
      var Number = $("#number").val();
      var Date = $("#date").val();
      var CVV = $("#cvv").val();
      var Card = $("#card").val();
		  var complete = false;

		  if(Name != "" && Address != "" && Number != "" && Date != "" && CVV != "" && Card != 1) {
			  complete = true;
		  }

		  if( Name == "") {
		    $("#fname").addClass("error").closest("label").addClass("error");
      }
        
      if(Address == "") {
			  $("#address").addClass("error").closest("label").addClass("error");
      }
        
      if(Number == "") {
			  $("#number").addClass("error").closest("label").addClass("error");
      }

      if(Date == "") {
			  $("#date").addClass("error").closest("label").addClass("error");
      }

      if(CVV == "") {
			  $("#cvv").addClass("error").closest("label").addClass("error");
      }

      //Checks if the card value is equal to the default selection value, 1
      //This means that the user has not selected a card type
      if( Card == 1) {
			  $("#card").addClass("error").closest("label").addClass("error");
      }
        
		  if(complete) {
			  $("#booking form button").html("Complete").attr('disabled', true);
      }
        
		  else {
			  $("#booking form button").html("Invalid");
		  }

		  event.preventDefault();

    });

    //Clears error class when values submitted
    $("#fname, #address, #number, #date, #cvv, #card").keydown(function() {
    $(this).removeClass("error").closest("label").removeClass("error");
    
	});
});