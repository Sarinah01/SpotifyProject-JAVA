-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 22, 2025 at 08:08 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `listener's_hub`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `getAdminActions` ()   BEGIN
SELECT * from admin_actions;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllArtists` ()   BEGIN
SELECT * FROM artist_data;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllMovies` ()   BEGIN 
SELECT * FROM movie_data;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllSongs` ()   BEGIN
SELECT * FROM songs;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAllUsers` ()   BEGIN
select * from user;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getMaxId` ()   BEGIN 
SELECT max(userId) FROM user;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `admin_actions`
--

CREATE TABLE `admin_actions` (
  `action_detail` varchar(50) NOT NULL,
  `action_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin_actions`
--

INSERT INTO `admin_actions` (`action_detail`, `action_time`) VALUES
('Admin logged in', '2025-08-19 04:32:44'),
('Displayed all users', '2025-08-19 04:32:48'),
('Removed user with ID: 121', '2025-08-19 04:35:02'),
('Admin logged in', '2025-08-19 04:42:03'),
('Added new song: Galliyaan', '2025-08-19 04:44:44'),
('Displayed all songs', '2025-08-19 04:46:00'),
('Removed song with ID: 9', '2025-08-19 04:46:11'),
('Admin logged in', '2025-08-19 05:04:50'),
('Added new artist: abc', '2025-08-19 05:04:59'),
('Displayed all artists', '2025-08-19 05:05:05'),
('Removed artist with ID: 51', '2025-08-19 05:05:13'),
('Displayed all artists', '2025-08-19 05:05:20'),
('Displayed admin actions', '2025-08-19 05:05:32'),
('Admin logged in', '2025-08-20 15:22:56'),
('[32mDisplayed all songs[0m', '2025-08-20 15:22:59'),
('Admin logged in', '2025-08-20 15:24:02'),
('Displayed all songs', '2025-08-20 15:24:09'),
('Admin logged in', '2025-08-20 15:25:39'),
('Displayed all songs', '2025-08-20 15:25:40'),
('Admin logged in', '2025-08-20 15:28:02'),
('Displayed all users', '2025-08-20 15:33:44'),
('Displayed all artists', '2025-08-20 15:34:16'),
('Admin logged in', '2025-08-21 04:43:48'),
('Displayed all movies', '2025-08-21 04:44:00'),
('Admin logged in', '2025-08-21 04:45:26'),
('Admin logged in', '2025-08-21 04:49:36'),
('Added new movie: bdb', '2025-08-21 04:49:48'),
('Displayed all movies', '2025-08-21 04:50:02'),
('Removed movie with ID: 6', '2025-08-21 04:50:39'),
('Admin logged in', '2025-08-22 13:26:00'),
('Added new movie: Dil se', '2025-08-22 13:27:33'),
('Added new song: Chal Chhaiya Chhaiya', '2025-08-22 13:29:31');

-- --------------------------------------------------------

--
-- Table structure for table `artist_data`
--

CREATE TABLE `artist_data` (
  `artist_id` int(9) NOT NULL,
  `name` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `artist_data`
--

INSERT INTO `artist_data` (`artist_id`, `name`) VALUES
(1, 'Arijit Singh'),
(2, 'Shreya Ghoshal'),
(3, 'Armaan Malik'),
(4, 'Neha Kakkar'),
(5, 'Sonu Nigam'),
(6, 'Lata Mangeshkar'),
(7, 'Kishore Kumar'),
(8, 'Asha Bhosle'),
(9, 'Mohit Chauhan'),
(12, 'Kumar Sanu'),
(13, 'Alka Yagnik'),
(14, 'Badshah'),
(15, 'Yo Yo Honey Singh'),
(18, 'Udit Narayan'),
(19, 'Ankit Tiwari'),
(20, 'Palak Muchhal'),
(21, 'Atif Aslam'),
(22, 'Sidhu Moose Wala'),
(23, 'Diljit Dosanjh'),
(24, 'Raftaar'),
(25, 'B Praak'),
(26, 'Darshan Raval'),
(27, 'Amit Trivedi'),
(28, 'Pritam'),
(29, 'Vishal-Shekhar'),
(30, 'Ajay-Atul'),
(31, 'Rahat Fateh Ali Khan'),
(32, 'Shilpa Rao'),
(33, 'Rekha Bhardwaj'),
(34, 'Tulsi Kumar'),
(35, 'Sukhwinder Singh'),
(36, 'Hardy Sandhu'),
(37, 'Jass Manak'),
(38, 'Neeti Mohan'),
(39, 'Salim Merchant'),
(40, 'Shaan'),
(41, 'Baapi Lahiri'),
(42, 'Mika Singh'),
(43, 'Himesh Reshammiya'),
(44, 'Rochak Kohli'),
(45, 'Payal Dev'),
(46, 'Tony Kakkar'),
(47, 'Akhil'),
(48, 'Ammy Virk'),
(49, 'Maninder Buttar'),
(50, 'Nikhita Gandhi'),
(51, 'Shubh'),
(52, 'Ed Sheeran'),
(53, 'Umesh Barot'),
(54, 'Aditya Gadhvi');

-- --------------------------------------------------------

--
-- Table structure for table `liked_songs`
--

CREATE TABLE `liked_songs` (
  `song_id` int(11) NOT NULL,
  `userId` int(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `liked_songs`
--

INSERT INTO `liked_songs` (`song_id`, `userId`) VALUES
(6, 101),
(4, 117),
(5, 117),
(1, 101),
(2, 101);

-- --------------------------------------------------------

--
-- Table structure for table `movie_data`
--

CREATE TABLE `movie_data` (
  `movie_id` int(11) NOT NULL,
  `Movie_name` varchar(30) NOT NULL,
  `Release_year` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `movie_data`
--

INSERT INTO `movie_data` (`movie_id`, `Movie_name`, `Release_year`) VALUES
(1, 'Genius ', 2018),
(2, 'Laal Singh Chaddha', 2022),
(3, 'Sanam Re', 2016),
(4, 'Ek Villain', 2014),
(5, 'Housefull 5', 2025),
(7, 'Kabir Singh', 2019),
(8, 'Dil se', 1998),
(9, 'Taal', 1999),
(10, 'Agneepath', 2012),
(11, 'Saajan', 1991),
(12, 'Kabhi Alvida Na Kehna', 2006),
(13, 'Kalaakaar', 1983),
(14, 'Blackmail', 1973),
(15, 'Jo Jeeta Wohi Sikandar ', 1992),
(16, 'Parineeta', 2005),
(17, 'Aashiqui 2', 2013),
(18, 'Saiyaara', 2025),
(19, 'Jannat', 2008),
(20, 'Yeh Jawaani Hai Deewani', 2013),
(21, 'Crook', 2010),
(22, 'Zeher', 2005),
(23, 'Manmarziyaan', 2018);

-- --------------------------------------------------------

--
-- Table structure for table `playlist`
--

CREATE TABLE `playlist` (
  `playlist_id` int(11) NOT NULL,
  `playlist_title` varchar(20) NOT NULL,
  `userId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `playlist`
--

INSERT INTO `playlist` (`playlist_id`, `playlist_title`, `userId`) VALUES
(1, 'Default Playlist 101', 101);

-- --------------------------------------------------------

--
-- Table structure for table `playlist_songs`
--

CREATE TABLE `playlist_songs` (
  `playlist_id` int(11) NOT NULL,
  `song_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `playlist_songs`
--

INSERT INTO `playlist_songs` (`playlist_id`, `song_id`) VALUES
(1, 4),
(1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `ratings`
--

CREATE TABLE `ratings` (
  `userId` int(11) NOT NULL,
  `song_id` int(11) NOT NULL,
  `Rating` decimal(2,1) NOT NULL DEFAULT 0.0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ratings`
--

INSERT INTO `ratings` (`userId`, `song_id`, `Rating`) VALUES
(120, 7, 4.5),
(120, 1, 4.8),
(117, 4, 4.7),
(101, 6, 4.5);

--
-- Triggers `ratings`
--
DELIMITER $$
CREATE TRIGGER `rating_check_trigger` BEFORE INSERT ON `ratings` FOR EACH ROW BEGIN
  IF NEW.rating < 0 OR NEW.rating > 5 THEN
    SIGNAL SQLSTATE '45000' 
      SET MESSAGE_TEXT = 'Rating must be between 0 and 5';
  END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `recently_played`
--

CREATE TABLE `recently_played` (
  `userId` int(11) NOT NULL,
  `song_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `recently_played`
--

INSERT INTO `recently_played` (`userId`, `song_id`) VALUES
(101, 1),
(101, 8),
(101, 2),
(101, 4),
(101, 6),
(101, 8),
(101, 5),
(101, 1),
(101, 7),
(120, 7),
(120, 6),
(120, 6),
(120, 4),
(120, 2),
(102, 1),
(102, 4),
(102, 5),
(102, 4),
(102, 5),
(102, 4),
(102, 4),
(102, 6),
(102, 1),
(102, 6),
(102, 1),
(102, 7),
(102, 1),
(101, 5),
(101, 4),
(101, 1),
(101, 4),
(117, 6),
(117, 4),
(117, 7),
(117, 5),
(117, 1),
(117, 5),
(101, 6),
(120, 1),
(117, 4),
(117, 4),
(101, 5),
(101, 9),
(101, 2),
(101, 6),
(101, 5),
(101, 1),
(101, 9),
(101, 2),
(101, 4),
(101, 2),
(101, 8),
(101, 5),
(101, 6),
(121, 7),
(101, 8),
(101, 4),
(101, 6),
(101, 2),
(101, 1),
(101, 1),
(101, 4),
(101, 1),
(101, 2),
(101, 4),
(101, 4),
(101, 2),
(101, 4),
(101, 8),
(101, 7),
(101, 5),
(101, 6),
(101, 9),
(101, 4),
(101, 2),
(101, 7),
(101, 2),
(101, 2),
(101, 4),
(101, 2),
(101, 1),
(101, 5),
(101, 8),
(101, 1),
(101, 2),
(101, 9),
(101, 1),
(101, 9),
(101, 4),
(101, 8),
(101, 1);

-- --------------------------------------------------------

--
-- Table structure for table `songs`
--

CREATE TABLE `songs` (
  `song_id` int(11) NOT NULL,
  `song_type` varchar(20) NOT NULL,
  `title` varchar(100) NOT NULL,
  `audio_path` varchar(255) DEFAULT NULL,
  `no_likes` int(11) DEFAULT 0,
  `rating` decimal(2,1) DEFAULT 0.0,
  `artist_id` int(11) DEFAULT NULL,
  `Movie_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `songs`
--

INSERT INTO `songs` (`song_id`, `song_type`, `title`, `audio_path`, `no_likes`, `rating`, `artist_id`, `Movie_id`) VALUES
(1, 'Romantic', 'Tera Fitoor', 'D:\\Project_Sem-2\\Songs\\Tera Fitoor.mp3', 1, 4.8, 1, 1),
(2, 'Romantic', 'Tere Hawale', 'D:\\Project_Sem-2\\Songs\\Tere Hawale.mp3', 1, 0.0, 1, 2),
(3, 'pop', 'sapphire ', 'D:\\Project_Sem-2\\Songs\\Sapphire.mp3', 0, 0.0, 52, NULL),
(4, 'Romantic', 'Pehla Pyaar', 'D:\\Project_Sem-2\\Songs\\Pehla Pyaar.mp3', 1, 4.7, 3, 7),
(5, 'Romantic', 'Hua Hai Aaj Pehli Baar', 'D:\\Project_Sem-2\\Songs\\Hua Hai Aaj Pehli Baar.mp3', 1, 0.0, 3, 3),
(6, 'Hip-Hop', 'Millionaire ', 'D:\\Project_Sem-2\\Songs\\Millionaire.mp3', 1, 4.5, 15, NULL),
(7, 'Hip-Hop', 'Desi Kalakaar', 'D:\\Project_Sem-2\\Songs\\Desi Kalakaar.mp3', 0, 4.5, 15, NULL),
(8, 'Bollywood', 'Laal Pari', 'D:\\Project_Sem-2\\Songs\\Laal Pari.mp3', 0, 0.0, 15, 5),
(9, 'Romantic', 'Galliyaan', 'D:\\Project_Sem-2\\Songs\\Galliyaan.mp3', 0, 0.0, 19, 4),
(10, 'Sufi-folk', 'Chal Chhaiya Chhaiya', 'D:\\Project_Sem-2\\Songs\\Chal chhaiya chhaiya.mp3', 0, 0.0, 35, 8),
(11, 'Hip-Hop', 'Cheques', 'D:\\Project_Sem-2\\Songs\\Cheques.mp3', 0, 0.0, 51, NULL),
(12, 'Hip-Hop', 'Supreme', 'D:\\Project_Sem-2\\Songs\\Supreme.mp3', 0, 0.0, 51, NULL),
(13, 'Hip-Hop', 'King Shit', 'D:\\Project_Sem-2\\Songs\\King Shit.mp3', 0, 0.0, 51, NULL),
(14, 'Rap', '295', 'D:\\Project_Sem-2\\Songs\\295.mp3', 0, 0.0, 22, NULL),
(15, 'Rap', 'So High', 'D:\\Project_Sem-2\\Songs\\So High.mp3', 0, 0.0, 22, NULL),
(16, 'Rap', 'Same Beef', 'D:\\Project_Sem-2\\Songs\\Same Beef.mp3', 0, 0.0, 22, NULL),
(17, 'Hip-Hop', 'Wakhra Swag', 'D:\\Project_Sem-2\\Songs\\Wakhra Swag.mp3', 0, 0.0, 14, NULL),
(18, 'Pop', 'Lemonade', 'D:\\Project_Sem-2\\Songs\\Lemonade.mp3', 0, 0.0, 23, NULL),
(19, 'Folk', 'Ramta Jogi', 'D:\\Project_Sem-2\\Songs\\Ramta Jogi.mp3', 0, 0.0, 35, 9),
(20, 'Folk', 'Chikni Chameli', 'D:\\Project_Sem-2\\Songs\\Chikni Chameli.mp3', 0, 0.0, 2, 10),
(21, 'Romantic', 'Mera Dil Bhi Kitna Pagal Hai', 'D:\\Project_Sem-2\\Songs\\Mera Dil Bhi Kitna Pagal Hai.mp3', 0, 0.0, 12, 11),
(22, 'Pop', 'Mitwa', 'D:\\Project_Sem-2\\Songs\\Mitwa.mp3', 0, 0.0, 12, 12),
(23, 'Pop', 'Neele Neele Ambar Par', 'D:\\Project_Sem-2\\Songs\\Neele Neele Ambar Par.mp3', 0, 0.0, 7, 13),
(24, 'Romantic', 'Pal Pal Dil Ke Paas', 'D:\\Project_Sem-2\\Songs\\Pal Pal Dil Ke Paas.mp3', 0, 0.0, 7, 14),
(25, 'Romantic Pop', 'Pehla Nasha', 'D:\\Project_Sem-2\\Songs\\Pehla Nasha.mp3', 0, 0.0, 18, 15),
(26, 'Romantic', 'Piyu bole', 'D:\\Project_Sem-2\\Songs\\Piyu bole.mp3', 0, 0.0, 5, 16),
(27, 'Emotional', 'Sun Raha Hai Na Tu', 'D:\\Project_Sem-2\\Songs\\Sun Raha Hai Na Tu.mp3', 0, 0.0, 19, 17),
(28, 'Emotional', 'Saiyaara', 'D:\\Project_Sem-2\\Songs\\Saiyaara.mp3', 0, 0.0, 2, 18),
(29, 'Pop', 'Shape of You', 'D:\\Project_Sem-2\\Songs\\Shape of You.mp3', 0, 0.0, 52, NULL),
(30, 'Folk', 'Gori Tame Manda Lidha Mohi Raj', 'D:\\Project_Sem-2\\Songs\\Gori Tame Manda Lidha Mohi Ra.mp3', 0, 0.0, 53, NULL),
(31, 'Folk', 'Khalaasi', 'D:\\Project_Sem-2\\Songs\\Khalaasi.mp3', 0, 0.0, 54, NULL),
(32, 'Romantic Folk', 'Nayan Ne Bandh Rakhine', 'D:\\Project_Sem-2\\Songs\\Nayan Ne Bandh Rakhine.mp3', 0, 0.0, 26, NULL),
(33, 'Romantic', 'Zara Sa', 'D:\\Project_Sem-2\\Songs\\Zara Sa.mp3', 0, 0.0, 28, 19),
(34, 'Sufi-folk', 'Kabira', 'D:\\Project_Sem-2\\Songs\\Kabira.mp3', 0, 0.0, 28, 20),
(35, 'Pop', 'Illahi', 'D:\\Project_Sem-2\\Songs\\Illahi.mp3', 0, 0.0, 28, 20),
(36, 'Romantic', 'Mere Bina', 'D:\\Project_Sem-2\\Songs\\Mere Bina.mp3', 0, 0.0, 28, 21),
(37, 'Romantic Pop', 'Pehli Dafa', 'D:\\Project_Sem-2\\Songs\\Pehli Dafa.mp3', 0, 0.0, 21, NULL),
(38, 'Emotional', 'Woh Lamhe Woh Baatein', 'D:\\Project_Sem-2\\Songs\\Woh Lamhe Woh Baatein.mp3', 0, 0.0, 21, 22),
(39, 'Emotional', 'Qismat', 'D:\\Project_Sem-2\\Songs\\Qismat.mp3', 0, 0.0, 48, NULL),
(40, 'Emotional', 'Daryaa', 'D:\\Project_Sem-2\\Songs\\Daryaa.mp3', 0, 0.0, 48, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userId` int(11) NOT NULL,
  `username` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL,
  `passwordHash` varchar(30) NOT NULL,
  `gender` varchar(10) NOT NULL,
  `mobileNumber` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userId`, `username`, `email`, `passwordHash`, `gender`, `mobileNumber`) VALUES
(101, 'kashyap', 'kashyap@gmail.com', 'pass123', 'Male', '9999990001'),
(102, 'riya', 'riya@example.com', 'riya123', 'Female', '9999990002'),
(103, 'ajay', 'ajay@example.com', 'ajay789', 'Male', '9999990003'),
(104, 'neha', 'neha@example.com', 'neha234', 'Female', '9999990004'),
(105, 'vikas', 'vikas@example.com', 'vikas567', 'Male', '9999990005'),
(106, 'sana', 'sana@example.com', 'sana789', 'Female', '9999990006'),
(107, 'arjun', 'arjun@example.com', 'arjun321', 'Male', '9999990007'),
(108, 'megha', 'megha@example.com', 'megha654', 'Female', '9999990008'),
(109, 'rohit', 'rohit@example.com', 'rohit000', 'Male', '9999990009'),
(110, 'tina', 'tina@example.com', 'tina222', 'Female', '9999990010'),
(111, 'dev', 'dev@example.com', 'dev123', 'Male', '9999990011'),
(112, 'sakshi', 'sakshi@example.com', 'sakshi456', 'Female', '9999990012'),
(113, 'manav', 'manav@example.com', 'manav789', 'Male', '9999990013'),
(114, 'isha', 'isha@example.com', 'isha234', 'Female', '9999990014'),
(115, 'rahul', 'rahul@example.com', 'rahul567', 'Male', '9999990015'),
(116, 'aarti', 'aarti@example.com', 'aarti890', 'Female', '9999990016'),
(117, 'yash', 'yash@example.com', 'yash321', 'Male', '9999990017'),
(118, 'khushi', 'khushi@example.com', 'khushi654', 'Female', '9999990018'),
(119, 'paras', 'paras@example.com', 'paras000', 'Male', '9999990019'),
(120, 'diya', 'diya@example.com', 'diya222', 'Female', '9999990020'),
(121, 'Harshh', 'harshh@gmail.com', 'Harshhx@#69', 'Male', '9724427001');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `artist_data`
--
ALTER TABLE `artist_data`
  ADD PRIMARY KEY (`artist_id`);

--
-- Indexes for table `liked_songs`
--
ALTER TABLE `liked_songs`
  ADD KEY `fk_user` (`userId`),
  ADD KEY `fk_song` (`song_id`);

--
-- Indexes for table `movie_data`
--
ALTER TABLE `movie_data`
  ADD PRIMARY KEY (`movie_id`);

--
-- Indexes for table `playlist`
--
ALTER TABLE `playlist`
  ADD PRIMARY KEY (`playlist_id`),
  ADD UNIQUE KEY `unique_playlist_title` (`playlist_title`),
  ADD KEY `userId` (`userId`);

--
-- Indexes for table `playlist_songs`
--
ALTER TABLE `playlist_songs`
  ADD KEY `playlist_id` (`playlist_id`),
  ADD KEY `song_id` (`song_id`);

--
-- Indexes for table `ratings`
--
ALTER TABLE `ratings`
  ADD KEY `userId` (`userId`),
  ADD KEY `song_id` (`song_id`);

--
-- Indexes for table `recently_played`
--
ALTER TABLE `recently_played`
  ADD KEY `recently_played_ibfk_1` (`userId`),
  ADD KEY `recently_played_ibfk_2` (`song_id`);

--
-- Indexes for table `songs`
--
ALTER TABLE `songs`
  ADD PRIMARY KEY (`song_id`),
  ADD KEY `artist_id` (`artist_id`),
  ADD KEY `fk_movie` (`Movie_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `artist_data`
--
ALTER TABLE `artist_data`
  MODIFY `artist_id` int(9) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;

--
-- AUTO_INCREMENT for table `movie_data`
--
ALTER TABLE `movie_data`
  MODIFY `movie_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT for table `playlist`
--
ALTER TABLE `playlist`
  MODIFY `playlist_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `songs`
--
ALTER TABLE `songs`
  MODIFY `song_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=122;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `liked_songs`
--
ALTER TABLE `liked_songs`
  ADD CONSTRAINT `fk_song` FOREIGN KEY (`song_id`) REFERENCES `songs` (`song_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_user` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `playlist`
--
ALTER TABLE `playlist`
  ADD CONSTRAINT `playlist_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `playlist_songs`
--
ALTER TABLE `playlist_songs`
  ADD CONSTRAINT `playlist_songs_ibfk_1` FOREIGN KEY (`playlist_id`) REFERENCES `playlist` (`playlist_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `playlist_songs_ibfk_2` FOREIGN KEY (`song_id`) REFERENCES `songs` (`song_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `ratings`
--
ALTER TABLE `ratings`
  ADD CONSTRAINT `ratings_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE,
  ADD CONSTRAINT `ratings_ibfk_2` FOREIGN KEY (`song_id`) REFERENCES `songs` (`song_id`) ON DELETE CASCADE;

--
-- Constraints for table `recently_played`
--
ALTER TABLE `recently_played`
  ADD CONSTRAINT `recently_played_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `recently_played_ibfk_2` FOREIGN KEY (`song_id`) REFERENCES `songs` (`song_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `songs`
--
ALTER TABLE `songs`
  ADD CONSTRAINT `fk_movie` FOREIGN KEY (`Movie_id`) REFERENCES `movie_data` (`movie_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `songs_ibfk_1` FOREIGN KEY (`artist_id`) REFERENCES `artist_data` (`artist_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
