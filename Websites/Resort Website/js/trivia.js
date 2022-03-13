$(document).ready(function() {
//These functions are used for the trivia game

    function trivia(event) {
      //Gets the value of the email form
      var Email = $("#form-email").val();

            //Checks if the email form has been filled
            if(Email == "") {
              //If not, adds error class to email form and replaces text on submit button
              $("#form-email").addClass("error").closest("label").addClass("error");
              $("#trivia form button").html("Invalid");
              }
      

            else {
              //The trivia game checks the values when the email form is filled
              //Sets answers and questions correct
              var answer1 = "1986"
              var answer2 = "Ukraine"
              var answer4 = "Pripyat"
              var answer5 = 1
              var answer6 = 2
              var answer7 = 3
              var answer8 = 2
              var answer9 = 1
              var answer10 = 3
              var correct = 0
              
              //Resets displayed message and message colour
              var message = "";
              var messageColour = "";
              
              //Obtains the users answer to question 1 from the appropriate form and logs the info
              var q1 = parseInt($("#form-year").val());
              console.log("User Guess: " + q1);

              var q2 = ($("#form-country").val());
              console.log("User Guess: " + q2);

              var q3 = ($("#form-nation").val());
              console.log("User Guess: " + q3);

              var q4 = ($("#form-city").val());
              console.log("User Guess: " + q4);

              var q5 = ($("#form-deaths").val());
              console.log("User Guess: " + q5);

              var q6 = ($("#form-evac").val());
              console.log("User Guess: " + q6);

              var q7 = ($("#form-disaster").val());
              console.log("User Guess: " + q7);

              var q8 = ($("#form-effect").val());
              console.log("User Guess: " + q8);

              var q9 = ($("#form-trap").val());
              console.log("User Guess: " + q9);

              var q10 = ($("#form-tomb").val());
              console.log("User Guess: " + q10);
        
            //Checks each user answer against the stored answer
            if(q1 == answer1) {
              correct ++
              //Increases the amount of correct answers each time an answer matches
            }
            if(q2 == answer2) {
              correct ++
            }
            //Accounts for various correct answers
            if((q3 == "USSR") || (q3 == "Soviet Union") || (q3 == "Russia")) {
              correct ++
            }
            if(q4 == answer4) {
              correct ++
            }
            if(q5 == answer5) {
              correct ++
            }
            if(q6 == answer6) {
              correct ++
            }
            if(q7 == answer7) {
              correct ++
            }
            if(q8 == answer8) {
              correct ++
            }
            if(q9 == answer9) {
              correct ++
            }
            if(q10 == answer10) {
              correct ++
            }
            
            //Sets various messages in different colours according to amount correct
            if(correct == 10) {
              message = "You got " + correct + " out of 10. Perfect score! We should be working for you!";
              messageColour = "green";
            }

            else if(correct > 7 ) {
              message = "You got " + correct + " out of 10. Great score! You'd make a great addition to the team.";
              messageColour = "green";
            }

            else if(correct > 4) {
              message = "You got " + correct + " out of 10. Average score. You could do better.";
              messageColour = "yellow";
            }

            else if(correct > 0) {
              message = "You got " + correct + " out of 10. Ouch, that's a low score! I think you need to do some research on Wikipedia...";
              messageColour = "orange";
            }

            else if(correct == 0) {
              message = "You got " + correct + " out of 10. How do you even get that low? You do know what the Chernobyl Disaster is, right?";
              messageColour = "lightgreen";
            }

            //Displays the message in the approriate colour and toggles the submit button
            $("#trivia .Message").html(message).css("color", messageColour);
            $("#trivia form button").html("Complete").attr('disabled', true);
            
        }
      
    //Clears the error class when data is input
    $("#form-email").keydown(function() {
    $(this).removeClass("error").closest("label").removeClass("error");
  });
  
    //Prevents defailt behaviour
		event.preventDefault();
  }
  
  //Adds listen event to the submit button, triggering the trivia function
  $("#trivia form").submit(trivia);

});