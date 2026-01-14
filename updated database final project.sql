DROP DATABASE IF EXISTS social_media;
CREATE DATABASE social_media;
USE social_media;

-- 1. Users table
CREATE TABLE users (
    user_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    profile_photo_url VARCHAR(255) DEFAULT 'https://cdn-icons-png.flaticon.com/512/149/149071.png',
    bio VARCHAR(255),
    email VARCHAR(50) NOT NULL,
    password VARCHAR(255) DEFAULT '1234',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Post table (Updated with image_path)
CREATE TABLE post (
    post_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id INTEGER NOT NULL,
    caption VARCHAR(200),
    location VARCHAR(50),
    image_path VARCHAR(255), -- This stores "images/post1.jpg"
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 3. Comments table
CREATE TABLE comments (
    comment_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    comment_text VARCHAR(255) NOT NULL,
    post_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES post(post_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 4. Post likes table
CREATE TABLE post_likes (
    user_id INTEGER NOT NULL,
    post_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (post_id) REFERENCES post(post_id),
    PRIMARY KEY (user_id, post_id)
);

-- DISABLE CHECKS FOR BULK INSERT
SET foreign_key_checks = 0;



-- INSERT USERS
INSERT INTO users (username, bio, email) VALUES 
("kanavphull", "Hedonist yet Spiritual", "as1mobiles@gmail.com"),
("raj gupta", "Tech enthusiast", "admin@1shopbuy.com"),
("Sahib Singh", "Life is a journey", "12angeldesignworld@gmail.com"),
("Sakshi Warandani", "NITJ wish me on 23 jan", "deepak@24sevenindia.com"),
("dettol sharma", "Clean living", "the.yellow.gold@gmail.com"),
("sunil", "Hotel management", "deepak@24sevenindia.com"),
("sanjay", "Football lover", "deepak@24sevenindia.com"),
("Axel Sivert Anker", "Norwegian explorer", "gazender.686@gmail.com"),
("Steven", "Living life my way", "sravi07@yahoo.com"),
("Jack", "Welcome To My Profile", "contact@21fools.com"),
("Oliver", "Official Account", "the.yellow.gold@gmail.com"),
("James", "Wish Me On 3 October", "contact@21fools.com"),
("Charlie", "Nature lover", "sravi07@yahoo.com"),
("Harris", "Traditional values", "pawan.modi1@gmail.com"),
("Lewis", "Tech blogger", "as1mobiles@gmail.com"),
("Leo", "Gym Lover", "pawan.modi1@gmail.com"),
("Noah", "Digital marketer", "sunglasses.24@gmail.com"),
("Alfie", "Single and focused", "deepak@24sevenindia.com"),
("Rory", "Spreading positivity", "pawan.modi1@gmail.com"),
("Alexander", "Respect For All", "umesh.agarwal@24x7safebuy.com"),
("Max", "Instagram King", "as1mobiles@gmail.com"),
("Logan", "Foodie and chef", "101cartinfo@gmail.com"),
("Harry", "Software developer", "shyamsunder121@gmail.com"),
("Theo", "Photography enthusiast", "info@3coinsplus.com"),
("Thomas", "Travel blogger", "wasif1@2dayenterprises.com"),
("Brodie", "Music Addict", "shyamsunder121@gmail.com"),
("Archie", "Book reader", "wasif1@2dayenterprises.com"),
("Jacob", "Single and happy", "mail@3gmobileworld.in"),
("Finlay", "Fitness trainer", "mail@3gmobileworld.in");
 
 
insert into users (username, bio , email) values ("Hassan","HI classmates " , 'hassan@gmail.com');
-- INSERT POSTS 
INSERT INTO post (post_id, user_id, caption, location, image_path) VALUES
(1, 24, 'Capturing the perfect wildlife shot by the lake', 'Goa', 'images/post1.jpg'),
(2, 28, 'Fresh red cherries glistening with morning dew', 'Himachal Pradesh', 'images/post2.jpg'),
(3, 37, 'Breathtaking view above the clouds with my best friend', 'Italy', 'images/post3.jpg'),
(4, 23, 'Festive vibes with a snowy pinecone ornament', 'Mumbai', 'images/post4.jpg'),
(5, 35, 'A majestic owl perched in the twilight', 'Manali', 'images/post5.jpg'),
(6, 36, 'Incredible macro shot of a robber fly in action', 'Home', 'images/post6.jpg'),
(7, 37, 'Vibrant rainbow stretching over the calm water', 'Garden', 'images/post7.jpg'),
(8, 6, 'Curious kitten exploring the green grass', 'Delhi', 'images/post8.jpg'),
(9, 7, 'A magical walk through the misty forest', 'Rishikesh', 'images/post9.jpg'),
(10, 44, 'The long open road through the valley', 'Jaipur', 'images/post10.jpg');

INSERT INTO post (post_id, user_id, caption, location, image_path) VALUES
(11, 5, 'Group photo memories at Taj Mahal Patakha Chicken', 'Abbottabad', 'images/post11.jpg'),
(12, 32, 'A fiery red sunset melting into the horizon', 'Seaside', 'images/post12.jpg'),
(13, 4, 'Misty railway tracks cutting through the autumn forest', 'Woodland', 'images/post13.jpg'),
(14, 47, 'Spectacular city lights and fountains by the bay', 'Dubai', 'images/post14.jpg'),
(15, 30, 'Finding peace among the white daisies', 'Flower Garden', 'images/post15.jpg'),
(16, 35, 'Morning sunlight breaking through the pine trees', 'Forest Path', 'images/post16.jpg'),
(17, 9, 'Millions of stars shining over the rugged mountains', 'Highlands', 'images/post17.jpg'),
(18, 13, 'Breathtaking aerial view of the city skyline at dusk', 'Melbourne', 'images/post18.jpg'),
(19, 2, 'Portrait of a craftsman deeply focused on his work', 'Old City', 'images/post19.jpg'),
(20, 20, 'Little explorer discovering nature by the big tree', 'Park', 'images/post20.jpg');

INSERT INTO post (post_id, user_id, caption, location, image_path) VALUES
(21, 11, 'Golden vineyards glowing under the sunset sky', 'Vineyard Valley', 'images/post21.jpg'),
(22, 38, 'Focusing on the rugged beauty of the mountains', 'Mountain Pass', 'images/post22.jpg'),
(23, 4, 'Stargazing at the Milky Way over the dark peaks', 'Night Sky Reserve', 'images/post23.jpg'),
(24, 6, 'A soulful melody by the calm river', 'Riverbank', 'images/post24.jpg'),
(25, 14, 'Childhood wonder among the purple blooms', 'Wildflower Field', 'images/post25.jpg'),
(26, 14, 'Happy Corgi enjoying the sunny afternoon', 'Green Park', 'images/post26.jpg'),
(27, 16, 'Fresh white daisies blooming in the meadow', 'Meadow', 'images/post27.jpg'),
(28, 23, 'Curious kitten peeking from the tree', 'Tree Trunk', 'images/post28.jpg'),
(29, 9, 'A majestic portrait of my feline friend', 'Home', 'images/post29.jpg'),
(30, 15, 'Tranquil waterfalls and fountains in the garden', 'Water Garden', 'images/post30.jpg');
-- INSERT COMMENTS
INSERT INTO comments(comment_text, post_id, user_id) VALUES 
('Great review!', 26, 6),
('Amazing setup!', 29, 15),
('Nice photo!', 12, 17),
('Beautiful sunset', 1, 5);

-- 1. Location of User 
SELECT * FROM post
WHERE location IN ('Goa', 'Mumbai', 'Delhi');

-- 5. Most Liked Posts (FIXED: Removed user_id from SELECT to fix Group By logic)
SELECT post_id, COUNT(post_id) AS like_count
FROM post_likes
GROUP BY post_id
ORDER BY like_count DESC LIMIT 10;

-- 6. Average post per user
SELECT ROUND((COUNT(post_id) / COUNT(DISTINCT user_id)), 2) AS 'Average Post per User' 
FROM post;

-- 7. User who liked every single post (CHECK FOR BOT)
SELECT username, Count(*) AS num_likes 
FROM users 
INNER JOIN post_likes ON users.user_id = post_likes.user_id 
GROUP BY post_likes.user_id 
HAVING num_likes = (SELECT Count(*) FROM post); 

-- 8. User Never Commented
SELECT user_id, username AS 'User Never Comment'
FROM users
WHERE user_id NOT IN (SELECT user_id FROM comments);

-- 9. User who commented on every post (CHECK FOR BOT)
-- FIXED: Subquery must count Posts, not Comments
SELECT username, Count(*) AS num_comment 
FROM users 
INNER JOIN comments ON users.user_id = comments.user_id 
GROUP BY comments.user_id 
HAVING num_comment = (SELECT Count(*) FROM post); 

SET foreign_key_checks = 0;

DELETE FROM users WHERE username = 'Omnicron Larson';
update users set username = 'Atif khan' where username = 'sunil';

update users set username = 'Quaid khan' where username = 'Quaid khan khan';