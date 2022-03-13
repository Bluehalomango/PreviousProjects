function parseInfo () {
    //Gets the current game page ID and the associated array from local storage
    var currentID = JSON.parse(localStorage.getItem("gameID"));
    var gameArray = JSON.parse(localStorage.getItem("game"))[currentID-1];

    //Inserts the relevant game page's info into the HTML
    document.getElementById("type").innerHTML = localStorage.getItem("category");
    document.getElementById("story").innerHTML = gameArray[1];
    //Sets the image source
    source = "images/" + localStorage.getItem("category") + "/" +  gameArray[7] + ".jpg";
    document.getElementById("choiceImage").src = source;

    //Calculates the maximum correct score for the current game type
    var fullArray = JSON.parse(localStorage.getItem("game"));
    maxCorrect = 0;
    var i = 0;
    for (i = 0; i< fullArray.length; i++) {
        if (fullArray[i][6] == "TRUE"){
            maxCorrect++;
        }
    }
    maxCorrect = Math.ceil(maxCorrect/2);
    maxCorrect--;
    //Displays user's score out of max
    document.getElementById("score").innerHTML = "You got " + localStorage.getItem("count") + " out of " + maxCorrect + " correct."

    //Creates link back to the start of the current game
    var link = document.getElementById('gameLink');
    link.href = "game.html?disaster=" + localStorage.getItem("category");
    //Resets game page ID and score count to default
    localStorage.setItem('gameID', 1);
    localStorage.setItem('count', 0);
    }

function toInformation(){
    //Creates link back to the relevant disaster info page
    window.location.href = "information.php?test=diasaster_information"  + "&type=" + localStorage.getItem("category");
}

$(document).ready(function() {
    //Displays info when ready
    parseInfo();
});
