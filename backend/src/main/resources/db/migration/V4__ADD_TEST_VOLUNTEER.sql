INSERT INTO user (email) VALUES ('supertest@tets.com');
INSERT INTO volunteer (reputation_score, user_id) VALUES (0, (select id from user where email = 'supertest@tets.com'));
