<!DOCTYPE html>
<html lang="en">
<?php 
    //Sets the character set for the connection to UTF-8
    header('Content-type: text/html; charset=utf-8');
    //Makes a connection to our phpmyadmin database
    $username = "accessuser";
    $password = "";
    $connection = new PDO("mysql:host=localhost;dbname=Disasters;charset=utf8", $username, $password);
    if($connection === false){
        die("ERROR: Could not connect. " . mysqli_connect_error());
    }

    //Gets disaster category from the URL and uses it to find the info in diasaster_information database
    $category = ($_GET["type"]);
    $sql = "SELECT * FROM diasaster_information WHERE category='$category'";

    //Using the query and connection to our Disasters secondary database, gets the safety info
    foreach ($connection->query($sql) as $row) {       
        $tempcat = $row['category'];
        $tempinfo = $row['general_info'];
        $tempeffects = $row['effects'];
        $tempact = $row['actions'];
        $tempactdp1 = $row['action_dp1'];
        $tempactdp2 = $row['action_dp2'];
        $tempactdp3 = $row['action_dp3'];
        $tempactdp4 = $row['action_dp4'];
        $tempactdp5 = $row['action_dp5'];
        $tempactdp6 = $row['action_dp6'];
        $tempactdp7 = $row['action_dp7'];
        $tempactdp8 = $row['action_dp8'];
        //Creates an array with the safety info
        $returnArray = array($tempcat, $tempinfo, $tempeffects, $tempact, $tempactdp1,$tempactdp2, $tempactdp3,$tempactdp4, $tempactdp5,$tempactdp6, $tempactdp7,$tempactdp8);
    }

    $category = ($_GET["type"]);
    //Checks if category is Urban Fire. If so, changes to concatenated version
    if ($category == "Urban Fire") {
        $category = "UrbanFire";
    }
    //Creates query to retreieve info from table with the same name as the category.
    //This is for the CYOA game
    $sql = "SELECT * FROM " .$category;
    $returnStory = array();

    //Gets the CYOA game info and creates an array with each entry being an array associated with that game page
    foreach ($connection->query($sql) as $row) {
        $tempID = $row['ChoiceID'];
        $tempText = $row['ChoiceText'];
        $tempLeft = $row['LeftOption'];
        $tempRight = $row['RightOption'];
        $tempLID = $row['LeftID'];
        $tempRID = $row['RightID'];
        $tempEnd = $row['End'];
        $tempimg = $row['imageSource'];
        $tempArray = array($row['ChoiceID'], $tempText, $tempLeft, $tempRight, $tempLID, $tempRID, $tempEnd, $tempimg);
        $returnStory[] = $tempArray;
    }
?>

<script type="text/javascript"> 
//Passes the safety info and game info to the info.js file
var passedArray = <?php echo json_encode($returnArray) ?>;
var passedStory = <?php echo json_encode($returnStory) ?>;
</script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Disaster Information</title>
<link rel="stylesheet" href="style.css">
<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="js/info.js"></script>
<!-- Link for font and home icon -->
<link href="https://fonts.googleapis.com/css2?family=Montserrat&family=Oswald&display=swap" rel="stylesheet">
<link  href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">

<!-- Styling for windowed map -->
<style>
    #map{
      height:200px;
      width:100%;
    }
</style>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<script src="js/infoMap.js"></script>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA2JEtOjSxymLB5AkT2Rv-7DmJnjWPNSIc&callback=initMap"></script>
</head>

<body>
    
    <header>
        <nav>
            <!-- Home Button Icon and link -->
            <a id="home" href="index.html"><i class="fa fa-home"></i></a>
        </nav>
        <h1 id = "disasterTitle"></h1>
    </header>
    <main id="information">
        <section>
            <!-- Left third of page (Map and Stats) -->
            <div>
            <!-- Div for the windowed google map -->
                <div class="map">
                    <div id="map"></div>
                </div>
            </div>
            <aside>
            <!-- Aside element for disaster statistics and danger rating -->
                <h2>Statistics</h2>
                <h3 id = "rating"></h3>
                <p id = "disasterStats"></p>
            </aside>
        </section>
        <section>
        <!-- Right two-thirds of page (Disaster and Safety Info) -->
            <article>
                <h2>Description</h2>
                <p id = "disasterContent"></p>
                <button class="readButton" onclick="readMoreDescription()" id="readMore">Read More</button>
                <button class="readButton" onclick="readLessDescription()" id="readLessDescription">Read Less</button>
                <section id="more">
                <!-- Section for Safety Info immediately displayed (Before "Read More" button) -->
                    <h2>Safety Information</h2>
                    <h3>Categories</h3>
                        <p id = "category">
                        </p>
                    <br>
                    <h3><u>General Information</u></h3>
                    <br>
                        <p id = "info">       
                        </p>
                    <button class="readButton" onclick="readMoreSafety()" id="readMoreSafetyInfo">Read More</button>
                    <br>
                </section>
                <section id="more2">
                <!-- Section for Safety Info after "Read More" button -->
                    <h3><u>Effects on us</u></h3>
                    <br>
                        <p id = "effects">
                        </p>
                    <br>
                    <h3><u>What could I do</u></h3>
                    <br>
                        <p id = "actions">
                        </p>
                        
                        <p id = "dotPoints">
                        </p>
                </section>
                <button class="readButton" onclick="readLessSafety()" id="readLess">Read Less</button>
            </article>
            <!-- Link to CYOA disaster game -->
            <a class="button" id = "gameLink">Experience The Disaster!</a>
        </section>
    </main>
</body>
</html>