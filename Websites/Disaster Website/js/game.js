//Gets the current game page ID and the associated array from local storage
var currentID = JSON.parse(localStorage.getItem("gameID"));
var gameArray = JSON.parse(localStorage.getItem("game"))[currentID-1];

function parseInfo () {
    //Inserts the relevant game page's info into the HTML
    document.getElementById("type").innerHTML = localStorage.getItem("category");
    document.getElementById("story").innerHTML = gameArray[1];
    document.getElementById("Left").innerHTML = gameArray[2];
    document.getElementById("Left").value = parseInt(gameArray[4]);
    document.getElementById("Right").innerHTML = gameArray[3];
    document.getElementById("Right").value = parseInt(gameArray[5]);    
    //Sets the image source
    source = "images/" + localStorage.getItem("category") + "/" +  gameArray[7] + ".jpg";
    document.getElementById("choiceImage").src = source;
}

function toNextPage(id) {
    //Checks if the current game page is after the initial branch
    if (currentID != 1) {
        if (id == parseInt(parseInt(gameArray[5]))) {
            //If the user selects the correct choice (Right side choice), obtain and increase score
            counter = JSON.parse(localStorage.getItem("count"));
            counter++;
            //Sets the users score after it changes
            localStorage.setItem('count', counter);
        }
    }
    //Sets current page ID to the next page according to their selection
    localStorage.setItem('gameID', id);

    //Checks if the next page corresponds to an ending scenario
    if ((JSON.parse(localStorage.getItem("game"))[id-1][6]) == "TRUE") {
        //If so, changes the URL destination to the endgame version
        window.location.href = "endgame.html?disaster=" + localStorage.getItem("category");
    } else {
        //If not, sends player to next part of the game
        window.location.href = "game.html?disaster=" + localStorage.getItem("category");
    }
}

$(document).ready(function() {
    //Displays info when ready
    parseInfo();
});
