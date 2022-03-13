<?php
defined('BASEPATH') OR exit('No direct script access allowed');
class ajax extends CI_Controller
{
    public function game() {
        $this->load->model('game_model');
        $output = '';
        $query = '';
        if ($this->input->get('game')) {
            $query = $this->input->get('game');
        }
        $data = $this->game_model->fetch_data($query);
        if (!$data == null) {
            echo json_encode($data->result());
        } else {
            echo "";
        }
    }

    public function search() {
        $this->load->model('game_model');
        $output = '';
        $query = '';
        if ($this->input->get('search')) {
            $query = $this->input->get('search');
        }
        $components = explode(".", $query);
        $data = $this->game_model->search_page($components[2], $components[0], $components[1]);
        if (!$data == null) {
            echo json_encode($data->result());
        } else {
            echo "";
        }
    }  

    public function autocomplete() {
        $this->load->model('game_model');
        $output = '';
        $query = '';
        if ($this->input->get('search')) {
            $query = $this->input->get('search');
            if ($query != '') {
                $components = explode(".", $query);
                $data = $this->game_model->search_page($components[2], $components[0], $components[1]);
                if (!$data == null) {
                    echo json_encode($data->result());
                } else {
                    echo "";
                }
            }
        }
    }  
}


