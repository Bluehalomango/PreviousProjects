<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');
 class Game_model extends CI_Model{

    public function upload($filename, $path, $username) {
        $data = array(
            'game_id' => $id,
            'name' => $name,
            'dor' => $dor,
            'description' => $description
        );
        $query = $this->db->insert('games', $data);
    }

    function fetch_data($query) {
        if ($query == '') {
            return null;
        } else {
            $this->db->select("*");
            $this->db->from("games");
            $this->db->like('name', $query);
            $this->db->order_by('name', 'DESC');
            return $this->db->get();
        }
    }

    function id_fetch($id) {
        $this->db->select("*");
        $this->db->from("games");
        $this->db->where('game_id', $id);
        $this->db->limit(1);
        $results = $this->db->get();
        return ($this->parse_single($results));
    }

    function parse_single($results) {
        $game = [];
        foreach ($results->result_array() as $row) {
            $filename = base_url() . "uploads/" . $row['filename'];
            $game = array($row['name'], $row['game_id'], $filename, $row['description'], $row['popularity'], $row['dor']);
        }
        return ($game);
    }

    function store_page($user = null) {
        if ($user) {
            $user_likes = $this->get_user_recomm($user);
            if (count($user_likes) > 0) {
                $this->db->select("*");
                $this->db->from("games");
                $this->db->order_by('popularity', 'DESC');
                $results = $this->db->get();    
                $games = $this->parse_array($results);
                $collection = [];
                foreach ($games as $game) {
                    $game_tags = $this->get_tags($game[1]);
                    if (count($game_tags) > 0) {
                        foreach ($user_likes as $user_tag) {
                            foreach ($game_tags as $game_tag) {
                                if ($user_tag[0] == $game_tag) {
                                    $popularity = $game[4];
                                    $user_pop = $user_tag[1];
                                    $popularity = $popularity * (1 + ($user_pop));
                                    $game[4] = $popularity;
                                }
                            }
                        }
                    }
                    array_push($collection, $game);
                }
                $new_array = array();
                $sortable_array = array();
                foreach ($collection as $key => $game) {
                    $sortable_array[$key]  = $game[4];
                }
                arsort($sortable_array);
                $index = 0;
                foreach ($sortable_array as $key => $value) {
                    if ($index < 5) {
                        $new_array[$key] = $collection[$key];
                        $index++;
                    }
                }
                $return = [];
                foreach ($new_array as $key => $value) {
                    array_push($return, $value);
                }
                return $return;
            }
        }
        $this->db->select("*");
        $this->db->from("games");
        $this->db->order_by('popularity', 'DESC');
        $this->db->limit(5);
        $results = $this->db->get();    
        return ($this->parse_array($results));
    }

    function parse_array($results) {        
        $collection = [];
        $game = [];
        foreach ($results->result_array() as $row) {
            $filename = base_url() . "uploads/" . $row['filename'];
            $game = array($row['name'], $row['game_id'], $filename, $row['description'], $row['popularity']);
            array_push($collection, $game);
        }
        return ($collection);
    }

    function add_review($game_id, $rating, $review, $user, $userAbsolute) {
        $data = array(
            'review_id'    => 'NULL',
            'game_id' => $game_id,
            'user' => $user,
            'rating' => $rating,
            'review' => $review,
            'user_id' => $userAbsolute
        );
        $this->db->insert('reviews', $data);
    }

    function reviews($id) {
        $this->db->select("*");
        $this->db->from("reviews");
        $this->db->like('game_id', $id);
        $this->db->order_by('review_id', 'DESC');
        $results = $this->db->get();    
        return ($this->parse_review($results));
    }

    function parse_review($results) {        
        $collection = [];
        $review = [];
        foreach ($results->result_array() as $row) {
            $review = array($row['user'], $row['rating'], $row['review']);
            array_push($collection, $review);
        }
        return ($collection);
    }

    function search_page($query, $filter, $order) {
        $this->db->select("*");
        $this->db->from("games");
        $this->db->like('name', $query);
        $this->db->order_by($filter, $order);
        return $this->db->get();    
    }

    function update_popularity($game_id) {
        $game = $this->id_fetch($game_id);
        $oldPop = $game[4];
        $newPop = $oldPop + 1;
        $this->db->set('popularity', $newPop);
        $this->db->where('game_id', $game_id);
        $this->db->update('games'); 
    }

    function slideshow($game_id) {
        $this->db->select("*");
        $this->db->from("images");
        $this->db->where('game_id', $game_id);
        $images = $this->db->get();    
        return ($this->parse_slideshow($images));
    }

    function parse_slideshow($images) {        
        $collection = [];
        $review = [];
        foreach ($images->result_array() as $row) {
            $image = base_url() . "uploads/" . $row['image_name'];
            array_push($collection, $image);
        }
        return ($collection);
    }

    function check_user_review($game_id, $user) {
        $this->db->where('game_id', $game_id);
        $this->db->where('user_id', $user);
        $result = $this->db->get('reviews');
        if ($result->num_rows() == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    function game_rating($game_id) {
        $this->db->select_avg('rating');
        $this->db->from("reviews");
        $this->db->where('game_id', $game_id);
        $result = $this->db->get();  
        foreach ($result->result_array() as $row) {
            $return = $row['rating'];
        }
        return ($return);  
    }

    function check_for_reviews($game_id) {
        $this->db->select("*");
        $this->db->from("reviews");
        $this->db->like('game_id', $game_id);
        $result = $this->db->get();    
        if ($result->num_rows() >= 1) {
            return true;
        } else {
            return false;
        }
    }

    function add_wishlist($game_id, $user) {
        $data = array(
            'game_id' => $game_id,
            'user' => $user
        );
        $this->db->insert('wishlist', $data);
    }

    function remove_wishlist_game($game_id, $user) {
        $this->db->where('game_id', $game_id);
        $this->db->where('user', $user);
        $this->db->delete('wishlist');
    }

    function view_wishlist($user) {
        $this->db->select("game_id");
        $this->db->from("wishlist");
        $this->db->where('user', $user);
        $result = $this->db->get();   
        $collection = [];
        $game = [];
        foreach ($result->result_array() as $row) {
            $game = $this->id_fetch($row['game_id']);
            array_push($collection, $game);
        } 
        return ($collection);
    }

    function check_wishlist($game_id, $user) {
        $this->db->where('game_id', $game_id);
        $this->db->where('user', $user);
        $result = $this->db->get('wishlist');
        if ($result->num_rows() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public function get_tags($game_id) {
        $this->db->select("*");
        $this->db->from("game_tags");
        $this->db->where('game_id', $game_id);
        $result = $this->db->get();  
        $collection = [];
        foreach ($result->result_array() as $row) {
            $tag = $row['tag'];
            array_push($collection, $row['tag']);
        }  
        return ($collection);
    }

    public function update_recomm($game_id, $user) {
        $tags = $this->get_tags($game_id);
        if (count($tags) > 0) {
            foreach ($tags as $tag) {
                $oldPop = $this->get_user_pop($user, $tag);
                if ($oldPop == 0) {
                    $data = array(
                        'user' => $user,
                        'tag'  => $tag,
                        'popularity'  => ($oldPop + 1)
                    );
                    $this->db->insert('user_recommend', $data);
                } else {
                    $this->db->set('popularity', $oldPop + 1);
                    $search = array('user' => $user, 'tag' => $tag);
                    $this->db->where($search);
                    $this->db->update('user_recommend'); 
                }
            }
        }
    }

    public function get_user_pop($user, $tag) {
        $this->db->select("popularity");
        $this->db->from("user_recommend");
        $search = array('user' => $user, 'tag' => $tag);
        $this->db->where($search);
        $this->db->limit(1);
        $result = $this->db->get();  
        if ($result->num_rows() == 1) {
            foreach ($result->result_array() as $row) {
                $return = $row['popularity'];
            } 
            return $return;
        } else {
            return 0;
        }
    }

    public function get_user_recomm($user) {
        $this->db->select("*");
        $this->db->from("user_recommend");
        $this->db->where('user', $user);
        $result = $this->db->get();  
        $collection = [];
        $preference = [];
        foreach ($result->result_array() as $row) {
            $preference = array($row['tag'], $row['popularity']);
            array_push($collection, $preference);
        }  
        return ($collection);
    }

    public function check_tag($game_id, $tag) {
        $this->db->select("*");
        $this->db->from("game_tags");
        $this->db->where('game_id', $game_id);
        $this->db->where('tag', $tag);
        $result = $this->db->get();  
        if ($result->num_rows() == 1) {
            return false;
        } else {
            return true;
        }
    }

    function add_tag($game_id, $tag) {
        if ($this->check_tag($game_id, $tag)) {
            $data = array(
                'game_id' => $game_id,
                'tag' => $tag
            );
            $this->db->insert('game_tags', $data);
        }
    }

    function get_tag_types($game_id) { 
        $this->db->select("*");
        $this->db->from("tags");
        $result = $this->db->get();  
        $collection = [];
        foreach ($result->result_array() as $row) {
            $tag = $row['tag'];
            array_push($collection, $row['tag']);
        }  
        return ($collection);
    }
}
