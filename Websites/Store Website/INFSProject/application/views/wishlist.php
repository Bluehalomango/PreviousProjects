<section id = "wishlist_games">
</section>

<script>
load_data();

    function load_data(){
        var games = <?php echo json_encode($games); ?>;
        games.forEach(add_game);
    }

    function add_game(game_id, index) {
        var flex = [];
        var name = game_id[0];
        var dor = game_id[5];
        var year = dor.split('-', 1);
        var filename = game_id[2];
        var id = game_id[1];
        var title = "<article class = 'flex-item'> <h1 class = 'name'> " + name + ', ' + year + "</h1>" 
        var remove = "<h1 class = 'btn' onclick=remove_game(" + id + ")> Remove from Wishlist </h1>";
        var view = "<h1 class = 'btn' onclick=load_game(" + id + ")> View Game </h1>";
        var image = "<article class = 'flex-item1'> <img class = 'poster' src=" + filename + " /> </article>";
        var content = "<section class = 'wrapper'" + image + title + remove + view + "</section>";
        flex.push($(content));
        $('#wishlist_games').append.apply($('#wishlist_games'), flex);   
    }

    function remove_game(game_id) {
        var url = "<?php echo base_url(); ?>store/remove_wishlist/" + game_id;
        window.location.href = url;
    }
</script>

<style>

* {
	margin: 0;
	padding: 0;
}

.title {
    font-size: 3em;
    padding: 20px;
}

.options {
    font-size:0.8em;
}

#wishlist_games {
    width: 80vw;
    border-style: solid;
    margin:auto;
    border-color: black;
}

.discussion > * {
    margin-top: 10px;
}

.poster {
    max-width: 25vw;
    max-height: 25vw;
}

.discussion {
    display: flex;  
    flex-flow: row nowrap;
    margin: 20px;
    border-color: black;
    border-style: solid;
}

.flex_aside {
    width: 70%;
    padding: 10px;
    line-height: 150px;
}

.views_aside {
    width: 30%;
    padding: 20px;
    line-height: 150px;
    text-align: right;
}

.topic {
    font-size: 2em;
}

.subtext {
    font-size: 1em;
}

.wrap {
    background: purple;
    width: 90vw;
    margin: 5%;
}

.btn {
   margin: 2%;
   background-color: #4CAF50; 
   border: none;
   color: white;
   padding: 0.5em 1em;
   text-align: center;
   display: inline-block;
   font-size: 1.2em;
}
</style>