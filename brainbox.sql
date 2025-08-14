create database brainbox_db;
use  brainbox_db;
-- Insert users
INSERT INTO user (id, username, password) VALUES
(1, 'john', '$2a$12$BCofEJns6x.fntFusqThC.5yaKihBwEa6yCWc65jNCaae5Ms6wPWC'),   -- Admin (all roles)
(2, 'alice', '$2a$12$BCofEJns6x.fntFusqThC.5yaKihBwEa6yCWc65jNCaae5Ms6wPWC'),  -- Contributor
(3, 'bob', '$2a$12$BCofEJns6x.fntFusqThC.5yaKihBwEa6yCWc65jNCaae5Ms6wPWC'),    -- Editor
(4, 'seema', '$2a$12$BCofEJns6x.fntFusqThC.5yaKihBwEa6yCWc65jNCaae5Ms6wPWC'),  -- Reviewer
(5, 'manu', '$2a$12$BCofEJns6x.fntFusqThC.5yaKihBwEa6yCWc65jNCaae5Ms6wPWC');   -- Viewer

-- Insert user anita (id = 7)
INSERT INTO user (id, username, password) VALUES
(7, 'anita', '$2a$12$BCofEJns6x.fntFusqThC.5yaKihBwEa6yCWc65jNCaae5Ms6wPWC');


-- john = Admin (all roles)
INSERT INTO user_roles (user_id, role_id) VALUES
  (1, 1), -- ADMIN
  (1, 2), -- EDITOR
  (1, 3), -- CONTRIBUTOR
  (1, 4), -- REVIEWER
  (1, 5); -- VIEWER

-- alice = Contributor
INSERT INTO user_roles (user_id, role_id) VALUES
  (2, 3); -- CONTRIBUTOR

-- bob = Editor (and Contributor optionally)
INSERT INTO user_roles (user_id, role_id) VALUES
  (3, 2); -- EDITOR

-- seema = Reviewer
INSERT INTO user_roles (user_id, role_id) VALUES
  (4, 4); -- REVIEWER

-- manu = Viewer
INSERT INTO user_roles (user_id, role_id) VALUES
  (5, 5); -- VIEWER
  
  INSERT INTO user_roles (user_id, role_id) VALUES (7, 2);
  
  SELECT * FROM user;
SELECT * FROM role;
SELECT * FROM user_roles;






