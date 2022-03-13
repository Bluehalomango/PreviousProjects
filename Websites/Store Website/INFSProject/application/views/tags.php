<div class="container">
    <div class="col-4 offset-4">
		<?php echo form_open(base_url().'store/add_tag/'.$game_info[1]); ?>
		<h2 class="text-center">Add tags to <?= $game_info[0]?></h2>   
			<label for="tags">Add a Tag:</label>
			<select name="tags" id="tags">
			</select>
			<div class="form-group">
				<button type="submit" class="btn btn-primary btn-block">Add</button>
			</div>   
		<?php echo form_close(); ?>
	</div>
</div>


<script>
add_tags();

   function add_tags() {
      var tags = <?php echo json_encode($tags); ?>;
      for (i = 0; i < tags.length; i++) {
         var html = "<option value=" + tags[i] + ">" + tags[i] + "</option>";
         document.getElementById("tags").innerHTML += html;
      }
   }

</script>