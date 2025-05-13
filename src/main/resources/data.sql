INSERT INTO users (id, email, first_name, last_name, password, username)
VALUES
    (1, 'korotynskiy.viktor@gmail.com', 'Viktor', 'Korotynskiy', '{bcrypt}$2a$10$esNac50vASuCTJjF.mdhN.4YDjlqXOgK.ERevE286is0fQMx0Zopu', 'azoviktor'),
    (2, 'bohpob@fit.cvut.cz', 'Bohdan', 'Poberezhnyi', '{bcrypt}$2a$10$HadcUTm4HfpsqQeIom9FN.EREw29/M/YOFtbighCR2bbYpGGYIq5S', 'bohpob'),
    (3, 'stilinski@cvut.cz', 'Stiles', 'Stilinski', '{bcrypt}$2a$10$NZ3D0wzVMIiDq4LJL9kr1.hnaYbASPGMTbYmdYcVo4r2WRq0Mup7i', 'stilinski');

INSERT INTO user_roles (user_id, role)
VALUES
    (1, 'STUDENT'),
    (1, 'TEACHER'),
    (2, 'STUDENT'),
    (3, 'TEACHER');

INSERT INTO courses (id, code, title)
VALUES
    (1, 'CS101', 'Introduction to Computer Science'),
    (2, 'CS102', 'Data Structures and Algorithms');

INSERT INTO courses_students (course_id, student_id)
VALUES
    (1, 2),
    (2, 1),
    (2, 2);

INSERT INTO courses_teachers (course_id, teacher_id)
VALUES
    (1, 1),
    (2, 3);