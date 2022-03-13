<?php
defined('BASEPATH') OR exit('No direct script access allowed');
class Upload extends CI_Controller
{
    public function index() {
		$this->load->view('template/header'); 
    	if (!$this->session->userdata('logged_in')) {	
			if (get_cookie('remember')) { 
				$username = get_cookie('username'); 
				$password = get_cookie('password'); 
				if ($this->User_model->login($username, $password)) {
					$user_data = array('username' => $username,'logged_in' => true );
					$this->session->set_userdata($user_data); 
					$this->load->view('file',array('error' => ' ')); 
				}
			} else {
				redirect('login');
			}
		} else {
			$this->load->view('file',array('error' => ' '));
		}
		$this->load->view('template/footer');
	}
	
	public function images($game_id) {
		$this->load->view('template/header');
        $data ['game_id'] = $game_id;
		$this->load->view('image', $data);
	}

	public function uploadImages($game_id) {
		$imgReturn = '';
		$this->load->model('upload_model');
		if (!empty($_FILES['file']['name'])) {	
  			foreach ($_FILES['file']['name'] as $key => $val) {
				$imgName = $_FILES['file']['name'][$key];
				$path_parts = pathinfo($imgName);
				$allowed_types = array("jpg", "png", "jpeg");
				if (in_array($path_parts['extension'], $allowed_types)) {
					$path = '/var/www/htdocs/INFSProject/uploads/'.$imgName;
					move_uploaded_file($_FILES['file']['tmp_name'][$key], 'uploads/' . $val);
					$imgReturn .= '<img src="https://infs3202-fa0e88ae.uqcloud.net/INFSProject/uploads/'.$imgName.'" class="thumbnail" />';
					$this->upload_model->uploadImg($game_id, $imgName, $path);
				}
			}  
		}
		echo $imgReturn;
	}

    public function do_upload() {
		$this->load->model('upload_model');
        $config['upload_path'] = './uploads/';
		$config['allowed_types'] = 'jpg|jpeg|png';
		$config['max_size'] = 10000;
		$config['max_width'] = 19200;
		$config['max_height'] = 10800;
		$this->load->library('upload', $config);
		$this->load->helper('form');
		if (!$this->upload->do_upload('gamefile')) {
            $this->load->view('template/header');
            $data = array('error' => $this->upload->display_errors());
            $this->load->view('file', $data);
            $this->load->view('template/footer');
		} else {
			$name = $this->input->post('name');
			$dor = $this->input->post('dor');
			$descript = $this->input->post('description');
			$filename = $this->upload->data('file_name');
			$array = array(
				'game_id' => 'NULL',
				'name' => $name,
				'dor' => $dor,
				'description' => $descript,
				'filename' => $filename,
				'popularity' => '0'
			);
			$test = $this->upload->data('full_path');
			$this->upload_model->uploadGame($array, $this->upload->data('file_name'), $this->upload->data('full_path'));
			$this->load->view('template/header');
			$this->load->view('file', array('error' => 'File upload success. <br/>'));
			$this->load->view('template/footer');
		}
	}
}

