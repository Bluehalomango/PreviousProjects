-- phpMyAdmin SQL Dump
-- version 5.0.4deb2~bpo10+1+bionic1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 20, 2021 at 05:40 AM
-- Server version: 5.7.34-0ubuntu0.18.04.1
-- PHP Version: 7.2.24-0ubuntu0.18.04.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `INFSProject`
--

-- --------------------------------------------------------

--
-- Table structure for table `games`
--

CREATE TABLE `games` (
  `game_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `dor` date NOT NULL,
  `description` text NOT NULL,
  `filename` text NOT NULL,
  `popularity` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `games`
--

INSERT INTO `games` (`game_id`, `name`, `dor`, `description`, `filename`, `popularity`) VALUES
(19, 'Destiny 2', '2017-07-28', 'Dive into the world of Destiny 2 to explore the mysteries of the solar system and experience responsive first-person shooter combat. Unlock powerful elemental abilities and collect unique gear to customize your Guardian\'s look and playstyle. Enjoy Destiny 2’s cinematic story, challenging co-op missions, and a variety of PvP modes alone or with friends. Download for free today and write your legend in the stars.', 'lg.jpg', 43),
(20, 'Transistor', '2014-05-21', 'From the creators of Bastion, Transistor is a sci-fi themed action RPG that invites you to wield an extraordinary weapon of unknown origin as you fight through a stunning futuristic city. Transistor seamlessly integrates thoughtful strategic planning into a fast-paced action experience, melding responsive gameplay and rich atmospheric storytelling. During the course of the adventure, you will piece together the Transistor\'s mysteries as you pursue its former owners.\r\n', '539182.jpg', 1),
(21, 'Hyper Light Drifter', '2016-03-31', ' Echoes of a dark and violent past resonate throughout a savage land, steeped in treasure and blood. Hyper Light Drifter is an action adventure RPG in the vein of the best 16­bit classics, with modernized mechanics and designs on a much grander scale.\r\n\r\nDrifters of this world are the collectors of forgotten knowledge, lost technologies and broken histories. Our Drifter is haunted by an insatiable illness, traveling further into the lands of Buried Time, hoping to discover a way to quiet the vicious disease.', '20160606230146_1.jpg', 3),
(22, 'Killzone 3', '2011-02-22', ' Players will be tasked with utilising a host of new weapons and vehicles in the battle for human survival. Helghast variety is now greater than ever, with players facing jetpack troopers as well as enemies wielding weapons of mass destruction. Killzone 3 thoroughly explores the planet Helghan, showcasing a wide breadth of destructible environments and stunningly detailed scenery. Additionally, players will be tasked with utilizing a host of new weapons and vehicles in the war to stop the Helghast. Featuring an extensive single-player campaign and thrilling multi-player mode, Killzone 3 delivers a host of new gameplay elements and an intense storyline that will once again drive gamers to join the ranks of the ISA and fight the Helghast.', 'Killzone-Shadow-Fall-wallpaper-Best-Picture-Download.png', 1),
(23, 'Bastion', '2011-07-17', 'Bastion is an action role-playing experience that redefines storytelling in games, with a reactive narrator who marks your every move. Explore more than 40 lush hand-painted environments as you discover the secrets of the Calamity, a surreal catastrophe that shattered the world to pieces. Wield a huge arsenal of upgradeable weapons and battle savage beasts adapted to their new habitat. Finish the main story to unlock the New Game Plus mode and continue your journey!', 'bastion-22658-1920x1080.jpg', 41),
(24, 'Endless Legend', '2014-09-19', 'Another sunrise, another day of toil. Food must be grown, industries built, science and magic advanced, and wealth collected. Urgency drives these simple efforts, however, for your planet holds a history of unexplained apocalypse, and the winter you just survived was the worst on record. A fact that has also been true for the previous five.', 'Dk5q9ME.jpg', 1),
(25, 'FTL: Faster Than Light', '2012-09-15', 'In FTL you experience the atmosphere of running a spaceship trying to save the galaxy. It\'s a dangerous mission, with every encounter presenting a unique challenge with multiple solutions. What will you do if a heavy missile barrage shuts down your shields? Reroute all power to the engines in an attempt to escape, power up additional weapons to blow your enemy out of the sky, or take the fight to them with a boarding party? This \"spaceship simulation roguelike-like\" allows you to take your ship and crew on an adventure through a randomly generated galaxy filled with glory and bitter defeat.', 'FTL_eclipse_detail1.jpg', 3),
(26, 'Pyre', '2017-07-26', 'Pyre is a party-based RPG in which you lead a band of exiles to freedom through ancient competitions spread across a vast, mystical purgatory. Who shall return to glory, and who shall remain in exile to the end of their days?', 'GFDownside.jpg', 1),
(27, 'Halo 3: ODST', '2009-09-22', 'Halo 3: ODST comes to PC as the next installment in Halo: The Master Chief Collection. Now optimized for PC, experience the events preceding Halo 3 through the eyes of Orbital Drop Shock Troopers (ODST) as they return to familiar ground and attempt to uncover the motivations behind the Covenant’s invasion of New Mombasa. Isolated and vulnerable, use stealth and precision to survive the dangers of the Covenant occupation in a gripping new take on combat in the Halo universe.', 'halo3-odst_1920x1080.jpg', 1),
(28, 'Last of Us', '2013-06-14', 'Winner of over 200 Game of the Year awards, The Last of Us has been rebuilt for the PlayStation 4 system. Now featuring full 1080p, higher resolution character models, improved shadows and lighting, in addition to several other gameplay improvements. 20 years after a pandemic has radically changed known civilization, infected humans run wild and survivors are killing each other for food, weapons; whatever they can get their hands on. Joel, a violent survivor, is hired to smuggle a 14 year-old girl, Ellie, out of an oppressive military quarantine zone, but what starts as a small job soon transforms into a brutal journey across the U.S. The Last of Us Remastered includes the Abandoned Territories Map Pack, Reclaimed Territories Map Pack, and the critically acclaimed The Last of Us: Left Behind Single Player campaign that combines themes of survival, loyalty, and love with tense, survival-action gameplay. ', '338417.jpg', 91),
(29, 'Halo Reach', '2010-09-14', 'Halo: Reach comes to PC as the first installment of Halo: The Master Chief Collection. Now optimized for PC, experience the heroic story of Noble Team, a group of Spartans, who through great sacrifice and courage, saved countless lives in the face of impossible odds. The planet Reach is humanity’s last line of defense between the encroaching Covenant and their ultimate goal, the destruction of Earth. If it falls, humanity will be pushed to the brink of destruction.', 'image-reach-concept-art-halo-nation-the_263715.jpg', 7),
(30, 'Oneshot', '2016-12-01', 'A surreal puzzle adventure game with unique mechanics / capabilities. You are to guide a child through a mysterious world on a mission to restore its long-dead sun. ...Of course, things are never that simple. The world knows you exist. The consequences are real. Saving the world may be impossible. You only have one shot.', 'oneshot_fanart2.png', 1),
(31, 'Ori and the Blind Forest', '2015-05-11', ' The forest of Nibel is dying. After a powerful storm sets a series of devastating events in motion, an unlikely hero must journey to find his courage and confront a dark nemesis to save his home. “Ori and the Blind Forest” tells the tale of a young orphan destined for heroics, through a visually stunning action-platformer crafted by Moon Studios for PC. Featuring hand-painted artwork, meticulously animated character performance, and a fully orchestrated score, “Ori and the Blind Forest” explores a deeply emotional story about love and sacrifice, and the hope that exists in us all.', '2016-02-12_00019.jpg', 8);

-- --------------------------------------------------------

--
-- Table structure for table `game_tags`
--

CREATE TABLE `game_tags` (
  `game_id` int(11) NOT NULL,
  `tag` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `game_tags`
--

INSERT INTO `game_tags` (`game_id`, `tag`) VALUES
(19, 'Action'),
(22, 'Action'),
(27, 'Action'),
(29, 'Action'),
(21, 'Adventure'),
(23, 'Adventure'),
(25, 'Adventure'),
(28, 'Adventure'),
(30, 'Puzzle'),
(31, 'Puzzle'),
(19, 'RPG'),
(21, 'RPG'),
(23, 'RPG'),
(23, 'Story'),
(29, 'Story'),
(30, 'Story'),
(31, 'Story'),
(24, 'Strategy '),
(25, 'Strategy');

-- --------------------------------------------------------

--
-- Table structure for table `images`
--

CREATE TABLE `images` (
  `img_id` int(11) NOT NULL,
  `image_name` varchar(100) NOT NULL,
  `path` varchar(200) NOT NULL,
  `game_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `images`
--

INSERT INTO `images` (`img_id`, `image_name`, `path`, `game_id`) VALUES
(94, 'lg.jpg', '/var/www/htdocs/INFSProject/uploads/lg.jpg', 19),
(95, '539182.jpg', '/var/www/htdocs/INFSProject/uploads/539182.jpg', 20),
(96, '20160606230146_1.jpg', '/var/www/htdocs/INFSProject/uploads/20160606230146_1.jpg', 21),
(97, 'Killzone-Shadow-Fall-wallpaper-Best-Picture-Download.png', '/var/www/htdocs/INFSProject/uploads/Killzone-Shadow-Fall-wallpaper-Best-Picture-Download.png', 22),
(98, 'bastion-22658-1920x1080.jpg', '/var/www/htdocs/INFSProject/uploads/bastion-22658-1920x1080.jpg', 23),
(99, 'Dk5q9ME.jpg', '/var/www/htdocs/INFSProject/uploads/Dk5q9ME.jpg', 24),
(100, 'FTL_eclipse_detail1.jpg', '/var/www/htdocs/INFSProject/uploads/FTL_eclipse_detail1.jpg', 25),
(101, 'GFDownside.jpg', '/var/www/htdocs/INFSProject/uploads/GFDownside.jpg', 26),
(102, 'halo3-odst_1920x1080.jpg', '/var/www/htdocs/INFSProject/uploads/halo3-odst_1920x1080.jpg', 27),
(103, '338417.jpg', '/var/www/htdocs/INFSProject/uploads/338417.jpg', 28),
(104, 'image-reach-concept-art-halo-nation-the_263715.jpg', '/var/www/htdocs/INFSProject/uploads/image-reach-concept-art-halo-nation-the_263715.jpg', 29),
(105, '92024233_2554525938200595_3283363392391217152_o.jpg', '/var/www/htdocs/INFSProject/uploads/92024233_2554525938200595_3283363392391217152_o.jpg', 19),
(106, '92163075_2554525764867279_8804841690201325568_o.jpg', '/var/www/htdocs/INFSProject/uploads/92163075_2554525764867279_8804841690201325568_o.jpg', 19),
(107, 'destiny_500milhoes.jpg', '/var/www/htdocs/INFSProject/uploads/destiny_500milhoes.jpg', 19),
(108, 'destiny_art-5.jpg', '/var/www/htdocs/INFSProject/uploads/destiny_art-5.jpg', 19),
(109, 'Destiny_Rise_of_Iron_Concept_Art_by_Sung_Choi_foundry-gate.jpg', '/var/www/htdocs/INFSProject/uploads/Destiny_Rise_of_Iron_Concept_Art_by_Sung_Choi_foundry-gate.jpg', 19),
(110, 'destiny-13.jpg', '/var/www/htdocs/INFSProject/uploads/destiny-13.jpg', 19),
(111, 'destiny-39.jpg', '/var/www/htdocs/INFSProject/uploads/destiny-39.jpg', 19),
(112, '20160606230002_1.jpg', '/var/www/htdocs/INFSProject/uploads/20160606230002_1.jpg', 21),
(113, '20160606230007_1.jpg', '/var/www/htdocs/INFSProject/uploads/20160606230007_1.jpg', 21),
(114, '20160605012824_1.jpg', '/var/www/htdocs/INFSProject/uploads/20160605012824_1.jpg', 21),
(115, '270040.jpg', '/var/www/htdocs/INFSProject/uploads/270040.jpg', 28),
(117, 'the_last_of_us__fanart_by_jinkang-d627qr0.jpg', '/var/www/htdocs/INFSProject/uploads/the_last_of_us__fanart_by_jinkang-d627qr0.jpg', 28),
(118, 'the-last-of-us-remastered-wide.jpg', '/var/www/htdocs/INFSProject/uploads/the-last-of-us-remastered-wide.jpg', 28),
(119, 'vault-of-glass_destiny_bungie_1600x980_marked.jpg', '/var/www/htdocs/INFSProject/uploads/vault-of-glass_destiny_bungie_1600x980_marked.jpg', 19),
(120, 'oneshot_fanart2.png', '/var/www/htdocs/INFSProject/uploads/oneshot_fanart2.png', 30),
(121, 'bastion_wallpaper_8-HD.jpg', '/var/www/htdocs/INFSProject/uploads/bastion_wallpaper_8-HD.jpg', 23),
(122, 'bastion-22679-1920x1080.jpg', '/var/www/htdocs/INFSProject/uploads/bastion-22679-1920x1080.jpg', 23),
(123, 'GFCaelondia.jpg', '/var/www/htdocs/INFSProject/uploads/GFCaelondia.jpg', 23),
(124, '2016-02-12_00019.jpg', '/var/www/htdocs/INFSProject/uploads/2016-02-12_00019.jpg', 31),
(125, '2016-02-12_00020.jpg', '/var/www/htdocs/INFSProject/uploads/2016-02-12_00020.jpg', 31),
(126, '2016-02-10_00013.jpg', '/var/www/htdocs/INFSProject/uploads/2016-02-10_00013.jpg', 31),
(127, '2016-02-12_00011.jpg', '/var/www/htdocs/INFSProject/uploads/2016-02-12_00011.jpg', 31),
(128, '2016-02-12_00018.jpg', '/var/www/htdocs/INFSProject/uploads/2016-02-12_00018.jpg', 31),
(130, '1167912-gorgerous-halo-circles-wallpaper-1440x2560-for-iphone-7.jpg', '/var/www/htdocs/INFSProject/uploads/1167912-gorgerous-halo-circles-wallpaper-1440x2560-for-iphone-7.jpg', 29),
(132, 'Emile_a239_by_TDSpiral.png', '/var/www/htdocs/INFSProject/uploads/Emile_a239_by_TDSpiral.png', 29),
(133, 'destiny___iron_banner_wallpaper__mobile__by_overwatchgraphics-d84bc20.png', '/var/www/htdocs/INFSProject/uploads/destiny___iron_banner_wallpaper__mobile__by_overwatchgraphics-d84bc20.png', 19),
(134, 'e4ea63464915eedf0767346fcd35d031.jpg', '/var/www/htdocs/INFSProject/uploads/e4ea63464915eedf0767346fcd35d031.jpg', 19),
(135, 'bf2dc0959feb390025fad81613678d70.jpg', '/var/www/htdocs/INFSProject/uploads/bf2dc0959feb390025fad81613678d70.jpg', 19);

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews` (
  `review_id` int(11) NOT NULL,
  `game_id` int(11) NOT NULL,
  `user` varchar(100) NOT NULL,
  `rating` int(3) NOT NULL,
  `review` text NOT NULL,
  `user_id` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `reviews`
--

INSERT INTO `reviews` (`review_id`, `game_id`, `user`, `rating`, `review`, `user_id`) VALUES
(12, 19, 'username4', 9, ' One of my favourite games!', 'username4'),
(13, 28, 'username4', 10, ' A real heart-wrencher!', 'username4'),
(16, 28, 'username2', 7, ' Didn\'t have enough shooting in it.', 'username2'),
(18, 19, 'username2', 6, ' Can get real grindy after a while...', 'username2'),
(19, 23, 'username2', 9, ' One of the best games I\'ve played!', 'username2'),
(20, 19, 'username1', 8, ' Great game, been playing the series since the first.', 'username1'),
(21, 28, 'username1', 8, ' Fantastic story, meh gameplay...', 'username1'),
(22, 19, 'Anonymous User', 5, 'They\'ve been making some real questionable choices about the game lately, I recommended you hold out to see where they go.', 'username5'),
(23, 23, 'Anonymous User', 10, ' Phenomenal artstyle and soundtrack. It will make your ears cry.', 'username5');

-- --------------------------------------------------------

--
-- Table structure for table `tags`
--

CREATE TABLE `tags` (
  `tag` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tags`
--

INSERT INTO `tags` (`tag`) VALUES
('Action'),
('Adventure'),
('Puzzle'),
('RPG'),
('Story'),
('Strategy ');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `username` varchar(15) NOT NULL,
  `password` varchar(60) NOT NULL,
  `phone_number` varchar(15) NOT NULL,
  `address` varchar(200) NOT NULL,
  `email` varchar(100) NOT NULL,
  `verify` int(11) NOT NULL DEFAULT '0',
  `secret_question` text NOT NULL,
  `secret_answer` text NOT NULL,
  `hidden` tinyint(1) NOT NULL DEFAULT '0',
  `verify_code` int(6) NOT NULL,
  `salt` varchar(30) CHARACTER SET utf16le COLLATE utf16le_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `password`, `phone_number`, `address`, `email`, `verify`, `secret_question`, `secret_answer`, `hidden`, `verify_code`, `salt`) VALUES
('username1', 'df9f49881c822bee2f3aaa76b1f9233ef7af723a', '06', '54 Main St', 'bluehalomango@hotmail.co.uk', 0, 'What is your favourite colour?', 'Red', 0, 564467, '195f288d4e037'),
('username2', 'c91c76dc2e35748c091e5857d35a6135d24c6721', '', '', 'bluehalomango@gmail.com', 0, '', '', 1, 885187, '33c8ba0c82916'),
('username3', '0e5f00af6f9f38c9c02f257d094f90902b008d56', '0', '', '', 0, '', '', 0, 0, '1c6c4defd712'),
('username4', '790b9c11ee4cf9b0b23c1fb81ba62be669d3a523', '+61432595896', '47 Main st, Brisbane', 'bluehalomango@gmail.com', 1, '', '', 0, 477578, '2d9d7a58b20f6'),
('username5', '4d87a74de80b6edca6718b312972c34fe4cf314e', '', '', 'bluehalomango@gmail.com', 1, '', '', 1, 0, 'e56a480ad5cb'),
('username6', 'a777bb5883f63b6479b759ece1734000b2248b14', '0400011123', '', 'bluehalomango@gmail.com', 1, '', '', 0, 0, '2b3c53197ea71');

-- --------------------------------------------------------

--
-- Table structure for table `user_recommend`
--

CREATE TABLE `user_recommend` (
  `user` varchar(15) NOT NULL,
  `tag` varchar(15) NOT NULL,
  `popularity` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_recommend`
--

INSERT INTO `user_recommend` (`user`, `tag`, `popularity`) VALUES
('username1', 'Action', 14),
('username1', 'Adventure', 18),
('username1', 'Puzzle', 7),
('username1', 'RPG', 23),
('username1', 'Story', 8),
('username1', 'Strategy', 1),
('username2', 'Puzzle', 47),
('username2', 'Story', 18);

-- --------------------------------------------------------

--
-- Table structure for table `wishlist`
--

CREATE TABLE `wishlist` (
  `game_id` int(11) NOT NULL,
  `user` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `wishlist`
--

INSERT INTO `wishlist` (`game_id`, `user`) VALUES
(19, 'username1'),
(23, 'username1'),
(28, 'username1');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `games`
--
ALTER TABLE `games`
  ADD PRIMARY KEY (`game_id`);

--
-- Indexes for table `game_tags`
--
ALTER TABLE `game_tags`
  ADD PRIMARY KEY (`game_id`,`tag`),
  ADD KEY `tag` (`tag`);

--
-- Indexes for table `images`
--
ALTER TABLE `images`
  ADD PRIMARY KEY (`img_id`),
  ADD KEY `images_ibfk_1` (`game_id`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
  ADD PRIMARY KEY (`review_id`),
  ADD KEY `review_game` (`game_id`),
  ADD KEY `review_user` (`user_id`);

--
-- Indexes for table `tags`
--
ALTER TABLE `tags`
  ADD PRIMARY KEY (`tag`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `user_recommend`
--
ALTER TABLE `user_recommend`
  ADD PRIMARY KEY (`user`,`tag`),
  ADD KEY `recommend_tag` (`tag`);

--
-- Indexes for table `wishlist`
--
ALTER TABLE `wishlist`
  ADD PRIMARY KEY (`game_id`,`user`),
  ADD KEY `wishlist_user` (`user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `games`
--
ALTER TABLE `games`
  MODIFY `game_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `images`
--
ALTER TABLE `images`
  MODIFY `img_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=136;

--
-- AUTO_INCREMENT for table `reviews`
--
ALTER TABLE `reviews`
  MODIFY `review_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `game_tags`
--
ALTER TABLE `game_tags`
  ADD CONSTRAINT `game` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tag` FOREIGN KEY (`tag`) REFERENCES `tags` (`tag`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `images`
--
ALTER TABLE `images`
  ADD CONSTRAINT `images_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
  ADD CONSTRAINT `review_game` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `review_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`username`);

--
-- Constraints for table `user_recommend`
--
ALTER TABLE `user_recommend`
  ADD CONSTRAINT `recommend_tag` FOREIGN KEY (`tag`) REFERENCES `tags` (`tag`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `recommend_user` FOREIGN KEY (`user`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `wishlist`
--
ALTER TABLE `wishlist`
  ADD CONSTRAINT `wishlist_game` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `wishlist_user` FOREIGN KEY (`user`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
