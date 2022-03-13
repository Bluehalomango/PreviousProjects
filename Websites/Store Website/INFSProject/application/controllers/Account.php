<?php
defined('BASEPATH') OR exit('No direct script access allowed');
class Account extends CI_Controller
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
        $user = $this->session->userdata('username');
        $data ['collection'] = $this->get_info($user); 
        $this->load->view('account/account', $data);
        $this->load->view('template/footer');
      }
    }

    function get_info($user) {
      $this->load->model('user_model');
      $results = $this->user_model->account($user);
      if ($results) {
        return ($results);
      }
    }

    function settings ($name) {
      $this->load->helper('url');
      $data['error']= "";
      $data['text']= $name;
      $this->load->view('template/header'); 
      $this->load->view('account/settings', $data); 
      $this->load->view('template/footer'); 
    }

    public function change ($type) {
      $this->load->helper('form');
      $info = $this->input->post('val');
      $this->load->model('User_model');
      if ($type == "email") {
        if ($this->User_model->check_email($info)) {
          $data['error']= "That email address is already currently in use by another account";
          $data['text']= 'email';
          $this->load->view('template/header'); 
          $this->load->view('account/settings', $data); 
          $this->load->view('template/footer'); 
        } else {
          $this->User_model->change($type, $info, $_SESSION["username"]);
          $this->index();
        }
      } else {
        $this->User_model->change($type, $info, $_SESSION["username"]);
        $this->index();
      }
    }

    public function change_email ($type) {
      $this->load->helper('form');
      $info = $this->input->post('val');
      $this->load->model('User_model');
      $this->User_model->change($type, $info, $_SESSION["username"]);
      $this->index();
    }

    public function question () {
      $this->load->helper('form');
      $answer = $this->input->post('newAnswer');
      $question = $this->input->post('newQuestion');
      $this->load->model('user_model');
      if ($question && $answer) {
        $this->user_model->question($question, $answer, $_SESSION["username"]);
      }
      $this->load->view('template/header');
      $data['error']= "";
      $user =  $this->user_model->account($_SESSION["username"]);
      $data['question'] = $user[6];
      $data['answer']= $user[7];
      $this->load->view('account/question', $data);
    }

    public function hidden($current) {
      $new = !$current;
      $this->load->model('user_model');
      $this->user_model->change("hidden", $new, $_SESSION["username"]);
      $this->index();
    }

    public function verify() {
      $this->load->model('user_model');
      $code = random_int(100000, 999999);
      $email = $this->user_model->get_single("email", $_SESSION["username"]);
      $this->verify_email($code, $email);
      $this->user_model->change("verify_code", $code, $_SESSION["username"]);
      $this->load->view('template/header'); 
      $data['result'] = '';
      $this->load->view('account/verify', $data); 
      $this->load->view('template/footer'); 
    }

    public function verify_email($code, $email) {
      $msg = "Greetings. Your email verification code for your account is: ".$code;
      $config = Array(
          'protocol' => 'smtp',
          'smtp_host' => 'mailhub.eait.uq.edu.au',
          'smtp_port' => 25,
          'mailtype' => 'html',
          'charset' => 'iso-8859-1',
          'wordwrap' => TRUE ,
          'mailtype' => 'html',
          'starttls' => true,
          'newline' => "\r\n"
          );
      $this->email->initialize($config);
      $this->email->from('s4583074@student.uq.edu.au',get_current_user());
      $this->email->to($email);
      $this->email->subject("Account Verification");
      $this->email->message($msg);
      $this->email->send();
    }

    public function check_verify() {
      $this->load->helper('form');
      $inputCode = $this->input->post('code');
      $this->load->model('user_model');
      $this->load->view('template/header'); 
      if ($this->user_model->check_code($inputCode, $_SESSION["username"])) {
        $data['result']= "Congrats! Your account has been successfully verified.";
        $this->user_model->change("verify_code", 0, $_SESSION["username"]);
        $this->user_model->change("verify", 1, $_SESSION["username"]);
        $this->load->view('account/verified'); 
      } else {
        $data['result']= "Unfortunately, the code you input is incorrect. If this issue persists, try to get another code sent and attempt the process again.";
        $this->load->view('account/verify', $data); 
      }
      $this->load->view('template/footer'); 
    }
}
?>


