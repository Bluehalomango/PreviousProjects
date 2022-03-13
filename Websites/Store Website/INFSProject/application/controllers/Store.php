<?php
defined('BASEPATH') OR exit('No direct script access allowed');
class Store extends CI_Controller
{
    public function index()
    {
        $data ['collection'] = $this->get_info(); 
		$this->load->view('template/header'); 
		$this->load->view('store', $data);
        $this->load->view('template/footer');
    }

    function get_info() {
        $this->load->model('game_model');
        $user = NULL;
        if ($this->session->userdata('logged_in')) { 
            $user =  $_SESSION["username"];
        }
        $results = $this->game_model->store_page($user);
        return ($results);
    }

    public function add_review($game_id) {   
		$this->load->helper('form');
		$rating = $this->input->post('rating');
        $review = $this->input->post('review'); 
        $this->load->model('game_model');
        $this->load->model('user_model');
        if ($this->user_model->get_single("hidden", $_SESSION["username"])) {
            $this->game_model->add_review($game_id, $rating, $review, "Anonymous User", $_SESSION["username"]);
        } else {
            $this->game_model->add_review($game_id, $rating, $review, $_SESSION["username"], $_SESSION["username"]);
        }
        $this->game($game_id);
    }

    public function game($id) {
        $this->load->model('game_model');
        $this->game_model->update_popularity($id);
        if ($this->session->userdata('logged_in')) { 
            $this->game_model->update_recomm($id, $_SESSION["username"]);
        }
        $this->load_game($id);
    }

    public function load_game($id) {
        $this->load->model('game_model');
        $game = $this->game_model->id_fetch($id);
        if ($this->session->userdata('logged_in')) { 
            $data ['user_status'] = true;
            $data ['has_reviewed'] = $this->game_model->check_user_review($id, $_SESSION["username"]);
            $data ['on_wishlist'] = $this->game_model->check_wishlist($id, $_SESSION["username"]);
        } else {
            $data ['user_status'] = false;
            $data ['has_reviewed'] = NULL;
            $data ['on_wishlist'] = NULL;
        }
        $data ['game_info'] = $game;
        $data ['tags'] = $this->game_model->get_tags($id);
        $data ['reviews'] = $this->game_model->reviews($id);
        $data ['images'] = $this->game_model->slideshow($id);
        if ($this->game_model->check_for_reviews($id)) {
            $rating = number_format($this->game_model->game_rating($id), 1);
            $data ['rating'] = $rating."/10";
        } else {
            $data ['rating'] = "Not Yet Rated";
        }
		$this->load->view('template/header'); 
		$this->load->view('game', $data);
        $this->load->view('template/footer');
    }

    public function search() {
		$this->load->view('template/header'); 
		$this->load->view('search');
        $this->load->view('template/footer');
    }

    function get_search() {
        $this->load->model('forum_model');
        $results = $this->forum_model->forum_page();
        return ($results);
        if (!$results == null) {
            echo json_encode($results->result());
        } else {
            echo "";
        }
    }

    public function add_wishlist($game_id) {
        $user = $_SESSION["username"];
        $this->load->model('game_model');
        $this->game_model->add_wishlist($game_id, $user);
        $this->load_game($game_id);
    }

    public function wishlist() {
        $user = $_SESSION["username"];
        $this->load->model('game_model');
        $data ['games'] = $this->game_model->view_wishlist($user);
		$this->load->view('template/header'); 
		$this->load->view('wishlist', $data);
        $this->load->view('template/footer');
    }

    public function remove_wishlist($game_id) {
        $user = $_SESSION["username"];
        $this->load->model('game_model');
        $this->game_model->remove_wishlist_game($game_id, $user);
        $this->wishlist();
    }

    public function tags($game_id) {
        $this->load->model('game_model');
        $data ['game_info'] = $this->game_model->id_fetch($game_id);
        $data ['tags'] = $this->game_model->get_tag_types($game_id);
		$this->load->view('template/header'); 
		$this->load->view('tags', $data);
        $this->load->view('template/footer');
    }

    public function add_tag($game_id) {
        $this->load->helper('form');
        $tag = $this->input->post('tags');
        $this->load->model('game_model');
        $this->game_model->add_tag($game_id, $tag);
        $this->game($game_id);
    }
}
?>


