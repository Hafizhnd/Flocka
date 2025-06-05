-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 05 Jun 2025 pada 10.05
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `flocka`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `comments`
--

CREATE TABLE `comments` (
  `comment_id` varchar(100) NOT NULL,
  `thread_id` varchar(100) DEFAULT NULL,
  `user_id` varchar(100) DEFAULT NULL,
  `content` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `communities`
--

CREATE TABLE `communities` (
  `community_id` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `image` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `communities`
--

INSERT INTO `communities` (`community_id`, `name`, `description`, `created_by`, `created_at`, `image`) VALUES
('community_1749018955210_78', 'hashd', 'asjhdjahs', 'user_1749018600408_67', '2025-06-04 06:35:55', NULL),
('community_1749019023397_695', 'asjhdahj', 'askhda', 'user_1749018600408_67', '2025-06-04 06:37:03', NULL);

-- --------------------------------------------------------

--
-- Struktur dari tabel `community_members`
--

CREATE TABLE `community_members` (
  `community_id` varchar(100) NOT NULL,
  `user_id` varchar(100) NOT NULL,
  `joined_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `community_members`
--

INSERT INTO `community_members` (`community_id`, `user_id`, `joined_at`) VALUES
('community_1749018955210_78', 'user_1749018600408_67', '2025-06-04 06:35:55'),
('community_1749019023397_695', 'user_1749018600408_67', '2025-06-04 06:37:03');

-- --------------------------------------------------------

--
-- Struktur dari tabel `events`
--

CREATE TABLE `events` (
  `event_id` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `organizer` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `event_date` date DEFAULT NULL COMMENT 'Primary date of the event',
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `location` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT 0.00 COMMENT 'Cost of the event, 0.00 for free',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `events`
--

INSERT INTO `events` (`event_id`, `name`, `organizer`, `description`, `event_date`, `start_time`, `end_time`, `location`, `image`, `cost`, `created_at`) VALUES
('event_1748893485_669', 'Mastering Business Strategies', 'user_1748840298130_141', 'Explore proven business strategies and frameworks to scale your venture and achieve sustainable growth. Interactive Q&A session included.', '2025-07-22', '2025-07-22 14:00:00', '2025-07-22 16:30:00', 'Zoom Meeting', '/public/images/events/mastering_business_strategies.png', 75000.00, '2025-06-02 19:44:45'),
('event_1748893485_696', 'Making 3D Elements for Modern Site Design', 'user_1748840298130_141', 'A hands-on workshop with George Sanchez on creating stunning 3D elements using popular web technologies. Bring your laptop!', '2025-08-12', '2025-08-12 13:00:00', '2025-08-12 16:00:00', 'Graha Surveyor, Kemayoran', '/public/images/events/making_3d_elements.png', 0.00, '2025-06-02 19:44:45'),
('event_1748893485_941', 'Unlocking Your Entrepreneurial Potential', 'user_1748840298130_141', 'Join Henry Wotton & Jennifer Williams to discover your inner entrepreneur. Learn to identify opportunities, build a resilient mindset, and launch your ideas.', '2025-08-05', '2025-08-05 09:00:00', '2025-08-05 17:00:00', 'Jakarta Convention Center', '/public/images/events/unlocking_entrepreneurial_potential.png', 150000.00, '2025-06-02 19:44:45');

-- --------------------------------------------------------

--
-- Struktur dari tabel `interests`
--

CREATE TABLE `interests` (
  `interest_id` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `interests`
--

INSERT INTO `interests` (`interest_id`, `name`) VALUES
('9b168419-3f29-11f0-aac0-088fc3ffe03e', 'Software Development'),
('9b16862d-3f29-11f0-aac0-088fc3ffe03e', 'Web Development'),
('9b1686ac-3f29-11f0-aac0-088fc3ffe03e', 'Mobile App Development'),
('9b1686ce-3f29-11f0-aac0-088fc3ffe03e', 'Frontend Engineering'),
('9b1686e6-3f29-11f0-aac0-088fc3ffe03e', 'Backend Engineering'),
('9b1686fa-3f29-11f0-aac0-088fc3ffe03e', 'Full Stack Development'),
('9b168710-3f29-11f0-aac0-088fc3ffe03e', 'Machine Learning'),
('9b168725-3f29-11f0-aac0-088fc3ffe03e', 'Artificial Intelligence'),
('9b16873c-3f29-11f0-aac0-088fc3ffe03e', 'Data Science'),
('9b16874f-3f29-11f0-aac0-088fc3ffe03e', 'Data Analysis'),
('9b168763-3f29-11f0-aac0-088fc3ffe03e', 'Big Data'),
('9b168777-3f29-11f0-aac0-088fc3ffe03e', 'Cloud Computing'),
('9b16878d-3f29-11f0-aac0-088fc3ffe03e', 'Cybersecurity'),
('9b16879f-3f29-11f0-aac0-088fc3ffe03e', 'Blockchain'),
('9b1687b2-3f29-11f0-aac0-088fc3ffe03e', 'DevOps'),
('9b1687c3-3f29-11f0-aac0-088fc3ffe03e', 'UI/UX Design'),
('9b1687d6-3f29-11f0-aac0-088fc3ffe03e', 'Product Management'),
('9b1687ee-3f29-11f0-aac0-088fc3ffe03e', 'Project Management'),
('9b16bda3-3f29-11f0-aac0-088fc3ffe03e', 'Agile Methodology'),
('9b16bdea-3f29-11f0-aac0-088fc3ffe03e', 'Scrum'),
('9b16be04-3f29-11f0-aac0-088fc3ffe03e', 'Quality Assurance'),
('9b16be1c-3f29-11f0-aac0-088fc3ffe03e', 'Testing Automation'),
('9b16be2f-3f29-11f0-aac0-088fc3ffe03e', 'Game Development'),
('9b16be42-3f29-11f0-aac0-088fc3ffe03e', 'Embedded Systems'),
('9b16be58-3f29-11f0-aac0-088fc3ffe03e', 'IoT Development'),
('9b16be6c-3f29-11f0-aac0-088fc3ffe03e', 'Digital Marketing'),
('9b16be80-3f29-11f0-aac0-088fc3ffe03e', 'SEO'),
('9b16be99-3f29-11f0-aac0-088fc3ffe03e', 'Content Writing'),
('9b16beb1-3f29-11f0-aac0-088fc3ffe03e', 'Copywriting'),
('9b16bec6-3f29-11f0-aac0-088fc3ffe03e', 'Technical Writing'),
('9b16bed8-3f29-11f0-aac0-088fc3ffe03e', 'Graphic Design'),
('9b16bf14-3f29-11f0-aac0-088fc3ffe03e', 'Illustration'),
('9b16bf29-3f29-11f0-aac0-088fc3ffe03e', '3D Modeling'),
('9b16bf3b-3f29-11f0-aac0-088fc3ffe03e', 'Animation'),
('9b16bf65-3f29-11f0-aac0-088fc3ffe03e', 'Video Editing'),
('9b16bf78-3f29-11f0-aac0-088fc3ffe03e', 'Photography'),
('9b16bf8c-3f29-11f0-aac0-088fc3ffe03e', 'Videography'),
('9b16bfb4-3f29-11f0-aac0-088fc3ffe03e', 'Public Relations'),
('9b16bfc5-3f29-11f0-aac0-088fc3ffe03e', 'Social Media Management'),
('9b16bfda-3f29-11f0-aac0-088fc3ffe03e', 'Email Marketing'),
('9b16bfec-3f29-11f0-aac0-088fc3ffe03e', 'Sales Strategy'),
('9b16bffe-3f29-11f0-aac0-088fc3ffe03e', 'Customer Success'),
('9b16c00f-3f29-11f0-aac0-088fc3ffe03e', 'Customer Support'),
('9b16c021-3f29-11f0-aac0-088fc3ffe03e', 'Human Resources'),
('9b16c033-3f29-11f0-aac0-088fc3ffe03e', 'Recruiting'),
('9b16c045-3f29-11f0-aac0-088fc3ffe03e', 'Employee Training'),
('9b16c056-3f29-11f0-aac0-088fc3ffe03e', 'Accounting'),
('9b16c069-3f29-11f0-aac0-088fc3ffe03e', 'Bookkeeping'),
('9b16c07b-3f29-11f0-aac0-088fc3ffe03e', 'Finance Analysis'),
('9b16c08c-3f29-11f0-aac0-088fc3ffe03e', 'Investment Analysis'),
('9b16c09e-3f29-11f0-aac0-088fc3ffe03e', 'Business Strategy'),
('9b16c0b0-3f29-11f0-aac0-088fc3ffe03e', 'Entrepreneurship'),
('9b16c0c4-3f29-11f0-aac0-088fc3ffe03e', 'Leadership'),
('9b16c0d5-3f29-11f0-aac0-088fc3ffe03e', 'Public Speaking'),
('9b16c0e7-3f29-11f0-aac0-088fc3ffe03e', 'Negotiation'),
('9b16c0f8-3f29-11f0-aac0-088fc3ffe03e', 'Conflict Resolution'),
('9b16c10b-3f29-11f0-aac0-088fc3ffe03e', 'Teaching'),
('9b16c11f-3f29-11f0-aac0-088fc3ffe03e', 'Curriculum Design'),
('9b16c130-3f29-11f0-aac0-088fc3ffe03e', 'Translation'),
('9b16c141-3f29-11f0-aac0-088fc3ffe03e', 'Linguistics'),
('9b16c153-3f29-11f0-aac0-088fc3ffe03e', 'Legal Research'),
('9b16c165-3f29-11f0-aac0-088fc3ffe03e', 'Paralegal Studies'),
('9b16c176-3f29-11f0-aac0-088fc3ffe03e', 'Medical Research'),
('9b16c187-3f29-11f0-aac0-088fc3ffe03e', 'Healthcare Administration'),
('9b16c19c-3f29-11f0-aac0-088fc3ffe03e', 'Nursing'),
('9b16c1ce-3f29-11f0-aac0-088fc3ffe03e', 'Pharmacy'),
('9b16c1e0-3f29-11f0-aac0-088fc3ffe03e', 'Biotechnology'),
('9b16c1f0-3f29-11f0-aac0-088fc3ffe03e', 'Physics'),
('9b16c202-3f29-11f0-aac0-088fc3ffe03e', 'Mathematics'),
('9b16c214-3f29-11f0-aac0-088fc3ffe03e', 'Statistics'),
('9b16c225-3f29-11f0-aac0-088fc3ffe03e', 'Architecture'),
('9b16c236-3f29-11f0-aac0-088fc3ffe03e', 'Interior Design'),
('9b16c248-3f29-11f0-aac0-088fc3ffe03e', 'Civil Engineering'),
('9b16c25a-3f29-11f0-aac0-088fc3ffe03e', 'Mechanical Engineering'),
('9b16c26d-3f29-11f0-aac0-088fc3ffe03e', 'Electrical Engineering'),
('9b16c27e-3f29-11f0-aac0-088fc3ffe03e', 'Chemical Engineering'),
('9b16c290-3f29-11f0-aac0-088fc3ffe03e', 'Environmental Science'),
('9b16c2a3-3f29-11f0-aac0-088fc3ffe03e', 'Geographic Information Systems'),
('9b16c2b4-3f29-11f0-aac0-088fc3ffe03e', 'Urban Planning'),
('9b16c2c5-3f29-11f0-aac0-088fc3ffe03e', 'Logistics Management'),
('9b16c2d7-3f29-11f0-aac0-088fc3ffe03e', 'Supply Chain Management'),
('9b16c2e7-3f29-11f0-aac0-088fc3ffe03e', 'Warehouse Operations'),
('9b16c2f9-3f29-11f0-aac0-088fc3ffe03e', 'Manufacturing'),
('9b16c30a-3f29-11f0-aac0-088fc3ffe03e', 'Robotics'),
('9b16c31b-3f29-11f0-aac0-088fc3ffe03e', 'Automotive Technology'),
('9b16c32e-3f29-11f0-aac0-088fc3ffe03e', 'Aerospace Engineering'),
('9b16c340-3f29-11f0-aac0-088fc3ffe03e', 'Marine Engineering'),
('9b16c356-3f29-11f0-aac0-088fc3ffe03e', 'Astronomy'),
('9b16c369-3f29-11f0-aac0-088fc3ffe03e', 'Psychology'),
('9b16c37d-3f29-11f0-aac0-088fc3ffe03e', 'Sociology'),
('9b16c398-3f29-11f0-aac0-088fc3ffe03e', 'Political Science'),
('9b16c3b0-3f29-11f0-aac0-088fc3ffe03e', 'Philosophy'),
('9b16c3c8-3f29-11f0-aac0-088fc3ffe03e', 'History'),
('9b16c3f5-3f29-11f0-aac0-088fc3ffe03e', 'Economics');

-- --------------------------------------------------------

--
-- Struktur dari tabel `orders`
--

CREATE TABLE `orders` (
  `order_id` varchar(100) NOT NULL,
  `user_id` varchar(100) NOT NULL,
  `item_type` enum('event','space') NOT NULL,
  `item_id` varchar(100) NOT NULL,
  `item_name` varchar(255) NOT NULL,
  `item_image` varchar(255) DEFAULT NULL,
  `booked_start_datetime` datetime NOT NULL,
  `booked_end_datetime` datetime DEFAULT NULL,
  `quantity` int(11) DEFAULT 1,
  `amount_paid` decimal(10,2) NOT NULL,
  `currency` varchar(10) DEFAULT 'IDR',
  `order_status` enum('active','completed','cancelled','archived') DEFAULT 'active',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `orders`
--

INSERT INTO `orders` (`order_id`, `user_id`, `item_type`, `item_id`, `item_name`, `item_image`, `booked_start_datetime`, `booked_end_datetime`, `quantity`, `amount_paid`, `currency`, `order_status`, `created_at`, `updated_at`) VALUES
('order_1748982318713_381', 'user_1748840298130_141', 'event', 'event_1748893485_696', 'Making 3D Elements for Modern Site Design', '/public/images/events/making_3d_elements.png', '2025-08-12 06:00:00', '2025-08-12 09:00:00', 1, 0.00, 'IDR', 'active', '2025-06-03 20:25:18', '2025-06-03 20:25:18'),
('order_1748984490936_751', 'user_1748840298130_141', 'event', 'event_1748893484_135', 'Design Rethink', '/public/images/events/design_rethink.png', '2025-07-15 03:00:00', '2025-07-15 05:00:00', 1, 50000.00, 'IDR', 'active', '2025-06-03 21:01:30', '2025-06-03 21:01:30'),
('order_1748984500890_502', 'user_1748840298130_141', 'space', 'space_1748901153_88', 'GoWork Pondok Indah', '/public/images/spaces/spaces_5.png', '2025-06-04 04:01:00', '2025-06-04 05:01:00', 1, 200000.00, 'IDR', 'active', '2025-06-03 21:01:40', '2025-06-03 21:01:40'),
('order_1748984537064_347', 'user_1748840298130_141', 'space', 'space_1748901153_88', 'GoWork Pondok Indah', '/public/images/spaces/spaces_5.png', '2025-06-04 04:02:00', '2025-06-04 05:02:00', 1, 200000.00, 'IDR', 'active', '2025-06-03 21:02:17', '2025-06-03 21:02:17'),
('order_1748984760911_1', 'user_1748840298130_141', 'space', 'space_1748901153_95', 'Growink Coworking Space', '/public/images/spaces/spaces_3.png', '2025-06-04 04:05:00', '2025-06-04 05:05:00', 1, 150000.00, 'IDR', 'active', '2025-06-03 21:06:00', '2025-06-03 21:06:00'),
('order_1748984762910_333', 'user_1748840298130_141', 'space', 'space_1748901153_95', 'Growink Coworking Space', '/public/images/spaces/spaces_3.png', '2025-06-04 04:05:00', '2025-06-04 05:05:00', 1, 150000.00, 'IDR', 'active', '2025-06-03 21:06:02', '2025-06-03 21:06:02'),
('order_1748984764478_305', 'user_1748840298130_141', 'space', 'space_1748901153_95', 'Growink Coworking Space', '/public/images/spaces/spaces_3.png', '2025-06-04 04:06:00', '2025-06-04 05:06:00', 1, 150000.00, 'IDR', 'active', '2025-06-03 21:06:04', '2025-06-03 21:06:04'),
('order_1748984766530_520', 'user_1748840298130_141', 'space', 'space_1748901153_95', 'Growink Coworking Space', '/public/images/spaces/spaces_3.png', '2025-06-04 04:06:00', '2025-06-04 05:06:00', 1, 150000.00, 'IDR', 'active', '2025-06-03 21:06:06', '2025-06-03 21:06:06'),
('order_1749021002057_448', 'user_1749018600408_67', 'event', 'event_1748893484_135', 'Design Rethink', '/public/images/events/design_rethink.png', '2025-07-15 03:00:00', '2025-07-15 05:00:00', 1, 50000.00, 'IDR', 'active', '2025-06-04 07:10:02', '2025-06-04 07:10:02'),
('order_1749021014562_829', 'user_1749018600408_67', 'event', 'event_1748893484_135', 'Design Rethink', '/public/images/events/design_rethink.png', '2025-07-15 03:00:00', '2025-07-15 05:00:00', 1, 50000.00, 'IDR', 'active', '2025-06-04 07:10:14', '2025-06-04 07:10:14'),
('order_1749021046917_868', 'user_1749018600408_67', 'event', 'event_1748893485_669', 'Mastering Business Strategies', '/public/images/events/mastering_business_strategies.png', '2025-07-22 07:00:00', '2025-07-22 09:30:00', 1, 75000.00, 'IDR', 'active', '2025-06-04 07:10:46', '2025-06-04 07:10:46'),
('order_1749021084092_812', 'user_1749018600408_67', 'event', 'event_1748893485_669', 'Mastering Business Strategies', '/public/images/events/mastering_business_strategies.png', '2025-07-22 07:00:00', '2025-07-22 09:30:00', 1, 75000.00, 'IDR', 'active', '2025-06-04 07:11:24', '2025-06-04 07:11:24'),
('order_1749021114383_947', 'user_1749018600408_67', 'event', 'event_1748893484_135', 'Design Rethink', '/public/images/events/design_rethink.png', '2025-07-15 03:00:00', '2025-07-15 05:00:00', 1, 50000.00, 'IDR', 'active', '2025-06-04 07:11:54', '2025-06-04 07:11:54');

-- --------------------------------------------------------

--
-- Struktur dari tabel `quizzes`
--

CREATE TABLE `quizzes` (
  `quiz_id` varchar(100) NOT NULL,
  `question` text NOT NULL,
  `option1` varchar(100) NOT NULL,
  `option2` varchar(100) NOT NULL,
  `option3` varchar(100) DEFAULT NULL,
  `option4` varchar(100) DEFAULT NULL,
  `correct_answer` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `quizzes`
--

INSERT INTO `quizzes` (`quiz_id`, `question`, `option1`, `option2`, `option3`, `option4`, `correct_answer`, `created_at`) VALUES
('quiz_001', 'What is the capital of France?', 'Berlin', 'Paris', 'Madrid', 'Rome', 2, '2025-06-03 19:03:32'),
('quiz_002', 'Which planet is known as the Red Planet?', 'Earth', 'Mars', 'Jupiter', 'Saturn', 2, '2025-06-03 19:03:32'),
('quiz_003', 'What is the largest ocean on Earth?', 'Atlantic', 'Indian', 'Arctic', 'Pacific', 4, '2025-06-03 19:03:32'),
('quiz_004', 'Who wrote \"Hamlet\"?', 'Charles Dickens', 'William Shakespeare', 'Leo Tolstoy', 'Mark Twain', 2, '2025-06-03 19:03:32'),
('quiz_005', 'What is 2 + 2?', '3', '4', '5', '6', 2, '2025-06-03 19:03:32'),
('quiz_006', 'Which element has the chemical symbol O?', 'Gold', 'Oxygen', 'Silver', 'Hydrogen', 2, '2025-06-03 19:03:32'),
('quiz_007', 'What is the tallest mammal?', 'Elephant', 'Giraffe', 'Whale', NULL, 2, '2025-06-03 19:03:32'),
('quiz_008', 'In which year did World War II end?', '1945', '1939', '1918', '1942', 1, '2025-06-03 19:03:32'),
('quiz_009', 'What is the primary ingredient in guacamole?', 'Tomato', 'Avocado', 'Onion', 'Pepper', 2, '2025-06-03 19:03:32'),
('quiz_010', 'Which country is famous for kangaroos?', 'New Zealand', 'Australia', 'South Africa', NULL, 2, '2025-06-03 19:03:32'),
('quiz_011', 'What is the speed of light?', '300,000 km/s', '150,000 km/s', 'Light is instant', NULL, 1, '2025-06-03 19:03:32'),
('quiz_012', 'Who painted the Mona Lisa?', 'Vincent van Gogh', 'Pablo Picasso', 'Leonardo da Vinci', 'Claude Monet', 3, '2025-06-03 19:03:32'),
('quiz_013', 'What is the currency of Japan?', 'Won', 'Yuan', 'Yen', 'Dollar', 3, '2025-06-03 19:03:32'),
('quiz_014', 'Which of these is a programming language?', 'HTML', 'JPEG', 'Python', 'MP3', 3, '2025-06-03 19:03:32'),
('quiz_015', 'What is the square root of 81?', '7', '8', '9', '10', 3, '2025-06-03 19:03:32'),
('quiz_016', 'Is the Earth flat?', 'Yes', 'No', NULL, NULL, 2, '2025-06-03 19:03:32'),
('quiz_017', 'What color is a banana when ripe?', 'Yellow', 'Green', 'Red', NULL, 1, '2025-06-03 19:03:32'),
('quiz_018', 'How many continents are there?', '5', '6', '7', '8', 3, '2025-06-03 19:03:32'),
('quiz_019', 'What is the main component of Earth\'s atmosphere?', 'Oxygen', 'Nitrogen', 'Carbon Dioxide', NULL, 2, '2025-06-03 19:03:32'),
('quiz_020', 'Who is the first president of the United States?', 'Abraham Lincoln', 'George Washington', 'Thomas Jefferson', 'John Adams', 2, '2025-06-03 19:03:32');

-- --------------------------------------------------------

--
-- Struktur dari tabel `spaces`
--

CREATE TABLE `spaces` (
  `space_id` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `location` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `cost_per_hour` decimal(10,2) DEFAULT NULL,
  `opening_time` time DEFAULT NULL,
  `closing_time` time DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `spaces`
--

INSERT INTO `spaces` (`space_id`, `name`, `location`, `description`, `cost_per_hour`, `opening_time`, `closing_time`, `image`, `created_at`) VALUES
('space_1748901153_533', 'Infiniti Office', 'Gedung BEI Tower 1 Level 3, Unit 304, Daerah Khusus Ibukota Jakarta 12190', 'Premium serviced office and co-working space in the heart of Jakarta\'s financial district. Professional environment with top-tier amenities and high-speed internet.', 250000.00, '08:00:00', '18:00:00', '/public/images/spaces/spaces_2.png', '2025-06-02 21:52:33'),
('space_1748901153_857', 'Neighborwork', 'Zoom Meeting / Virtual', 'A vibrant virtual co-working community for remote professionals and freelancers. Flexible access to themed virtual rooms and online networking events.', 15000.00, '00:00:00', '23:59:59', '/public/images/spaces/spaces_1.png', '2025-06-02 21:52:33'),
('space_1748901153_874', 'WU Hub Coworking Space', 'Sabang, Jl. H. Agus Salim No.53, Jakarta Pusat', 'Affordable and accessible coworking space in central Jakarta. A great spot for students, solo entrepreneurs, and small teams needing a productive environment.', 75000.00, '07:00:00', '22:00:00', '/public/images/spaces/spaces_4.png', '2025-06-02 21:52:33'),
('space_1748901153_88', 'GoWork Pondok Indah', 'Pondok Indah Office Tower 2, 15th Floor Jl. Sultan Iskandar Muda Kav. V-TA, Pondok Indah, Kota Jakarta Selatan', 'Modern and stylish coworking space in South Jakarta offering stunning city views, flexible membership options, and a dynamic community.', 200000.00, '08:30:00', '19:00:00', '/public/images/spaces/spaces_5.png', '2025-06-02 21:52:33'),
('space_1748901153_95', 'Growink Coworking Space', 'Jakarta Convention Center Area', 'Creative and collaborative coworking hub located near the JCC, ideal for startups, artists, and innovators. Features regular community events and workshops.', 150000.00, '09:00:00', '21:00:00', '/public/images/spaces/spaces_3.png', '2025-06-02 21:52:33');

-- --------------------------------------------------------

--
-- Struktur dari tabel `streaks`
--

CREATE TABLE `streaks` (
  `streak_id` varchar(100) NOT NULL,
  `user_id` varchar(100) DEFAULT NULL,
  `current_streak` int(11) DEFAULT 0,
  `longest_streak` int(11) DEFAULT 0,
  `last_updated` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `threads`
--

CREATE TABLE `threads` (
  `thread_id` varchar(100) NOT NULL,
  `community_id` varchar(100) DEFAULT NULL,
  `user_id` varchar(100) DEFAULT NULL,
  `title` varchar(100) NOT NULL,
  `content` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `todos`
--

CREATE TABLE `todos` (
  `todo_id` varchar(100) NOT NULL,
  `user_id` varchar(100) DEFAULT NULL,
  `task_title` varchar(100) NOT NULL,
  `task_description` text DEFAULT NULL,
  `start_time` time DEFAULT NULL,
  `end_time` time DEFAULT NULL,
  `date` date DEFAULT NULL,
  `is_done` tinyint(1) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `todos`
--

INSERT INTO `todos` (`todo_id`, `user_id`, `task_title`, `task_description`, `start_time`, `end_time`, `date`, `is_done`, `created_at`, `updated_at`) VALUES
('todo_1748953401593_864', 'user_1748840298130_141', 'newest', 'newsest\n', '06:23:00', '08:23:00', '2025-06-18', 0, '2025-06-03 12:23:21', '2025-06-03 12:23:21'),
('todo_1748954050947_157', 'user_1748840298130_141', 'g', 'vbjj', '05:33:00', '10:33:00', '2025-06-20', 0, '2025-06-03 12:34:10', '2025-06-03 13:22:29'),
('todo_1748954122300_832', 'user_1748840298130_141', 'jc ifct', 'kgvgovoyvy', '06:35:00', '09:35:00', '2025-06-17', 0, '2025-06-03 12:35:22', '2025-06-03 12:35:22'),
('todo_1748954141644_559', 'user_1748840298130_141', 'ig oyv', 'utxitcic8t\n', '06:35:00', '09:35:00', '2025-06-17', 0, '2025-06-03 12:35:41', '2025-06-03 12:35:41'),
('todo_1748956635125_491', 'user_1748840298130_141', 'jg giic', 'jfgjcyi', '05:17:00', '08:17:00', '2025-06-25', 0, '2025-06-03 13:17:15', '2025-06-03 13:17:15'),
('todo_1748956963885_390', 'user_1748840298130_141', 'hhah', 'sbbebs\n', '06:22:00', '09:22:00', '2025-06-24', 0, '2025-06-03 13:22:43', '2025-06-03 13:22:43'),
('todo_1748958716498_745', 'user_1748840298130_141', 'sbeb', 'sbsb', '06:51:00', '09:51:00', '2025-06-09', 0, '2025-06-03 13:51:56', '2025-06-03 13:51:56');

-- --------------------------------------------------------

--
-- Struktur dari tabel `users`
--

CREATE TABLE `users` (
  `uid` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `username` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `profession` varchar(100) DEFAULT NULL,
  `gender` enum('male','female','other') DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `bio` text DEFAULT NULL,
  `profile_image_url` varchar(255) DEFAULT NULL,
  `current_streak` int(11) NOT NULL DEFAULT 0,
  `last_quiz_completed_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `users`
--

INSERT INTO `users` (`uid`, `name`, `username`, `email`, `password`, `created_at`, `updated_at`, `profession`, `gender`, `age`, `bio`, `profile_image_url`, `current_streak`, `last_quiz_completed_date`) VALUES
('user_1748412434540_627', 'Lisa Hanifatul', 'lisahanifat', 'lisahanifat@gmail.com', '$2b$10$/WQTI4l2a6VkZdXyxk6gfOu1Bq3Gw0CNz611/rrjNMeWQa8DeHhV.', '2025-05-28 06:07:14', '2025-05-28 06:08:35', 'Student', 'female', 20, 'lucu gemas', '/public/images/profiles/user_1748412434540_627-1748412515033.jpg', 0, NULL),
('user_1748840298130_141', 'hafizh nabil', 'hanabom', 'hanabom@gmail.com', '$2b$10$G63kwFoc2o2rfNYTdpxZkultdh/IKhsern9FyPCJzeqqkMvqO187u', '2025-06-02 04:58:18', '2025-06-03 19:57:38', 'programmer', 'male', 20, 'anything', '/public/images/profiles/user_1748840298130_141-1748840346197.jpg', 6, '2025-06-03'),
('user_1748845545763_572', 'Hafizh Nabil', 'hafizh', 'hafizhnabild@gmail.com', '$2b$10$2FsC9hqkxGN/O0Q/CevDUuLutQdlpgakI9pHKXzz7GQjXJ3bQBz3K', '2025-06-02 06:25:45', '2025-06-02 06:26:48', 'programmee', 'male', 20, 'anything', '/public/images/profiles/user_1748845545763_572-1748845605710.jpg', 0, NULL),
('user_1749018600408_67', 'Kelompok 3', 'kelompok3', 'kelompok3@gmail.com', '$2b$10$zNEzigTwHUfkxsPXIJQHg.s9agw.hK5lWbAm1uO2ugE5oykoKHZW2', '2025-06-04 06:30:00', '2025-06-04 07:01:32', 'PAMLAN', 'male', 20, 'capstone fc kelompok 3', '/public/images/profiles/user_1749018600408_67-1749018633799.jpg', 3, '2025-06-04');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user_interests`
--

CREATE TABLE `user_interests` (
  `user_id` varchar(100) NOT NULL,
  `interest_id` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `user_quizzes`
--

CREATE TABLE `user_quizzes` (
  `user_quiz_id` varchar(100) NOT NULL,
  `user_id` varchar(100) DEFAULT NULL,
  `quiz_id` varchar(100) DEFAULT NULL,
  `answer_given` int(11) DEFAULT NULL,
  `is_correct` tinyint(1) DEFAULT NULL,
  `completed_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `user_quizzes`
--

INSERT INTO `user_quizzes` (`user_quiz_id`, `user_id`, `quiz_id`, `answer_given`, `is_correct`, `completed_at`) VALUES
('uq_1748979558772_990', 'user_1748840298130_141', 'quiz_007', 0, 0, '2025-06-03 19:39:18'),
('uq_1748979596512_671', 'user_1748840298130_141', 'quiz_003', 0, 0, '2025-06-03 19:39:56'),
('uq_1748979625080_922', 'user_1748840298130_141', 'quiz_011', 300, 0, '2025-06-03 19:40:25'),
('uq_1748980459962_749', 'user_1748840298130_141', 'quiz_009', 0, 0, '2025-06-03 19:54:19'),
('uq_1748980469779_183', 'user_1748840298130_141', 'quiz_011', 300, 0, '2025-06-03 19:54:29'),
('uq_1748980658025_227', 'user_1748840298130_141', 'quiz_014', 0, 0, '2025-06-03 19:57:38'),
('uq_1749020391993_888', 'user_1749018600408_67', 'quiz_014', 0, 0, '2025-06-04 06:59:51'),
('uq_1749020403628_73', 'user_1749018600408_67', 'quiz_008', 1945, 0, '2025-06-04 07:00:03'),
('uq_1749020492205_447', 'user_1749018600408_67', 'quiz_017', 0, 0, '2025-06-04 07:01:32');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `comments`
--
ALTER TABLE `comments`
  ADD PRIMARY KEY (`comment_id`),
  ADD KEY `thread_id` (`thread_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeks untuk tabel `communities`
--
ALTER TABLE `communities`
  ADD PRIMARY KEY (`community_id`),
  ADD KEY `created_by` (`created_by`);

--
-- Indeks untuk tabel `community_members`
--
ALTER TABLE `community_members`
  ADD PRIMARY KEY (`community_id`,`user_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeks untuk tabel `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`event_id`);

--
-- Indeks untuk tabel `interests`
--
ALTER TABLE `interests`
  ADD PRIMARY KEY (`interest_id`);

--
-- Indeks untuk tabel `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeks untuk tabel `quizzes`
--
ALTER TABLE `quizzes`
  ADD PRIMARY KEY (`quiz_id`);

--
-- Indeks untuk tabel `spaces`
--
ALTER TABLE `spaces`
  ADD PRIMARY KEY (`space_id`);

--
-- Indeks untuk tabel `streaks`
--
ALTER TABLE `streaks`
  ADD PRIMARY KEY (`streak_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeks untuk tabel `threads`
--
ALTER TABLE `threads`
  ADD PRIMARY KEY (`thread_id`),
  ADD KEY `community_id` (`community_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeks untuk tabel `todos`
--
ALTER TABLE `todos`
  ADD PRIMARY KEY (`todo_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indeks untuk tabel `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`uid`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indeks untuk tabel `user_interests`
--
ALTER TABLE `user_interests`
  ADD PRIMARY KEY (`user_id`,`interest_id`),
  ADD KEY `interest_id` (`interest_id`);

--
-- Indeks untuk tabel `user_quizzes`
--
ALTER TABLE `user_quizzes`
  ADD PRIMARY KEY (`user_quiz_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `quiz_id` (`quiz_id`);

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `comments`
--
ALTER TABLE `comments`
  ADD CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`thread_id`) REFERENCES `threads` (`thread_id`),
  ADD CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`uid`);

--
-- Ketidakleluasaan untuk tabel `communities`
--
ALTER TABLE `communities`
  ADD CONSTRAINT `communities_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`uid`);

--
-- Ketidakleluasaan untuk tabel `community_members`
--
ALTER TABLE `community_members`
  ADD CONSTRAINT `community_members_ibfk_1` FOREIGN KEY (`community_id`) REFERENCES `communities` (`community_id`),
  ADD CONSTRAINT `community_members_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`uid`);

--
-- Ketidakleluasaan untuk tabel `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`uid`);

--
-- Ketidakleluasaan untuk tabel `streaks`
--
ALTER TABLE `streaks`
  ADD CONSTRAINT `streaks_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`uid`);

--
-- Ketidakleluasaan untuk tabel `threads`
--
ALTER TABLE `threads`
  ADD CONSTRAINT `threads_ibfk_1` FOREIGN KEY (`community_id`) REFERENCES `communities` (`community_id`),
  ADD CONSTRAINT `threads_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`uid`);

--
-- Ketidakleluasaan untuk tabel `todos`
--
ALTER TABLE `todos`
  ADD CONSTRAINT `todos_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`uid`);

--
-- Ketidakleluasaan untuk tabel `user_interests`
--
ALTER TABLE `user_interests`
  ADD CONSTRAINT `user_interests_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`uid`),
  ADD CONSTRAINT `user_interests_ibfk_2` FOREIGN KEY (`interest_id`) REFERENCES `interests` (`interest_id`);

--
-- Ketidakleluasaan untuk tabel `user_quizzes`
--
ALTER TABLE `user_quizzes`
  ADD CONSTRAINT `user_quizzes_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`uid`),
  ADD CONSTRAINT `user_quizzes_ibfk_2` FOREIGN KEY (`quiz_id`) REFERENCES `quizzes` (`quiz_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
