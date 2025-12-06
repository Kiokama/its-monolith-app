-- Drop existing objects (safer for repeated runs)
DROP TABLE IF EXISTS mcq_options CASCADE;
DROP TABLE IF EXISTS answers CASCADE;
DROP TABLE IF EXISTS submissions CASCADE;
DROP TABLE IF EXISTS questions CASCADE;
DROP TABLE IF EXISTS assessments CASCADE;

-- Create assessments
CREATE TABLE assessments (
  id SERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  total_points INTEGER NOT NULL
);

-- Single-table inheritance for questions (MCQ, ESSAY, ...)
CREATE TABLE questions (
  id SERIAL PRIMARY KEY,
  content TEXT NOT NULL,
  points INTEGER NOT NULL,
  assessment_id INTEGER NOT NULL REFERENCES assessments(id) ON DELETE CASCADE,
  question_type VARCHAR(50),
  correct_answer VARCHAR(255),
  rubric TEXT
);

-- MCQ Options table (ElementCollection)
CREATE TABLE mcq_options (
  question_id INTEGER NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
  option TEXT NOT NULL
);

-- Answers table
CREATE TABLE answers (
  id SERIAL PRIMARY KEY,
  submission_id INTEGER NOT NULL,
  question_id INTEGER NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
  answer TEXT,
  score INTEGER
);

-- Submissions table
CREATE TABLE submissions (
  id SERIAL PRIMARY KEY,
  student_id VARCHAR(255) NOT NULL,
  assessment_id INTEGER NOT NULL REFERENCES assessments(id) ON DELETE CASCADE,
  score INTEGER,
  submitted_at TIMESTAMPTZ
);

-- Add foreign key for answers.submission_id after submissions table created
ALTER TABLE answers
ADD CONSTRAINT fk_answers_submission
FOREIGN KEY (submission_id) REFERENCES submissions(id) ON DELETE CASCADE;

-- Insert sample assessments (6 assessments)
INSERT INTO assessments (id, title, description, total_points) VALUES
(1, 'Intro to Java - Quiz', 'Short introductory quiz about Java basics', 30),
(2, 'Algorithms - Midterm', 'Midterm with MCQs and essay questions', 100),
(3, 'Database Design - Final Exam', 'Comprehensive exam on database concepts', 50),
(4, 'Web Development - Quiz', 'Quick assessment on HTML, CSS, JavaScript', 40),
(5, 'Data Structures - Practical Test', 'Hands-on test on arrays, linked lists, trees', 60),
(6, 'Software Engineering - Case Study', 'Analysis and design of a software system', 80);

-- Assessment 1: Intro to Java - Quiz (30 points)
INSERT INTO questions (id, content, points, assessment_id, question_type, correct_answer) VALUES
(1, 'What is the keyword to define a class in Java?', 5, 1, 'MCQ', 'class'),
(2, 'Which of these is NOT a primitive data type?', 5, 1, 'MCQ', 'String'),
(3, 'Explain the difference between == and equals() in Java.', 10, 1, 'ESSAY', NULL),
(4, 'What is the purpose of the "static" keyword?', 5, 1, 'MCQ', 'To create class variables'),
(5, 'Describe method overloading with an example.', 10, 1, 'ESSAY', NULL);

INSERT INTO mcq_options (question_id, option) VALUES
(1, 'class'), (1, 'struct'), (1, 'object'), (1, 'def'),
(2, 'int'), (2, 'double'), (2, 'String'), (2, 'boolean'),
(4, 'To create class variables'), (4, 'To create private methods'), (4, 'To declare constants'), (4, 'To prevent inheritance');

-- Assessment 2: Algorithms - Midterm (100 points)
INSERT INTO questions (id, content, points, assessment_id, question_type, correct_answer) VALUES
(6, 'What is the time complexity of binary search?', 10, 2, 'MCQ', 'O(log n)'),
(7, 'Which sorting algorithm has O(n²) worst-case complexity?', 10, 2, 'MCQ', 'Bubble Sort'),
(8, 'Describe Dijkstra''s algorithm and its use-cases.', 20, 2, 'ESSAY', NULL),
(9, 'What is a hash table and what is O(1) lookup?', 15, 2, 'MCQ', 'A data structure for fast lookups'),
(10, 'Explain the concept of dynamic programming with a real example.', 25, 2, 'ESSAY', NULL),
(11, 'Which data structure is best for implementing a priority queue?', 10, 2, 'MCQ', 'Heap'),
(12, 'Compare BFS and DFS algorithms.', 10, 2, 'ESSAY', NULL);

INSERT INTO mcq_options (question_id, option) VALUES
(6, 'O(log n)'), (6, 'O(n)'), (6, 'O(n log n)'), (6, 'O(1)'),
(7, 'Bubble Sort'), (7, 'Quick Sort'), (7, 'Merge Sort'), (7, 'Heap Sort'),
(9, 'A data structure for fast lookups'), (9, 'A sorting algorithm'), (9, 'A graph structure'), (9, 'A tree structure'),
(11, 'Heap'), (11, 'Array'), (11, 'Queue'), (11, 'Stack');

-- Assessment 3: Database Design - Final Exam (50 points)
INSERT INTO questions (id, content, points, assessment_id, question_type, correct_answer) VALUES
(13, 'What does ACID stand for in databases?', 5, 3, 'MCQ', 'Atomicity, Consistency, Isolation, Durability'),
(14, 'Explain database normalization and its benefits.', 15, 3, 'ESSAY', NULL),
(15, 'What is a primary key?', 5, 3, 'MCQ', 'A unique identifier for records'),
(16, 'Design a database schema for an online store.', 15, 3, 'ESSAY', NULL),
(17, 'What is the difference between JOIN types (INNER, LEFT, RIGHT)?', 10, 3, 'MCQ', 'How they filter rows from related tables');

INSERT INTO mcq_options (question_id, option) VALUES
(13, 'Atomicity, Consistency, Isolation, Durability'), (13, 'Authentication, Cryptography, Isolation, Data'), (13, 'Application, Client, Integration, Database'), (13, 'Attributes, Column, Index, Data'),
(15, 'A unique identifier for records'), (15, 'A foreign key reference'), (15, 'An index on a column'), (15, 'A constraint on data type'),
(17, 'How they filter rows from related tables'), (17, 'The order of execution'), (17, 'The performance of queries'), (17, 'The security level');

-- Assessment 4: Web Development - Quiz (40 points)
INSERT INTO questions (id, content, points, assessment_id, question_type, correct_answer) VALUES
(18, 'What does HTML stand for?', 5, 4, 'MCQ', 'HyperText Markup Language'),
(19, 'Explain the CSS Box Model.', 10, 4, 'ESSAY', NULL),
(20, 'Which CSS property is used for spacing outside an element?', 5, 4, 'MCQ', 'margin'),
(21, 'What is the difference between var, let, and const in JavaScript?', 10, 4, 'MCQ', 'Scope and reassignability'),
(22, 'Write a simple HTML form with validation.', 10, 4, 'ESSAY', NULL);

INSERT INTO mcq_options (question_id, option) VALUES
(18, 'HyperText Markup Language'), (18, 'Hyperlinks and Text Markup Language'), (18, 'High Tech Markup Language'), (18, 'Home Tool Markup Language'),
(20, 'margin'), (20, 'padding'), (20, 'border'), (20, 'outline'),
(21, 'Scope and reassignability'), (21, 'Only memory allocation'), (21, 'Only naming conventions'), (21, 'Only hoisting behavior');

-- Assessment 5: Data Structures - Practical Test (60 points)
INSERT INTO questions (id, content, points, assessment_id, question_type, correct_answer) VALUES
(23, 'What is the time complexity of inserting at the beginning of a linked list?', 8, 5, 'MCQ', 'O(1)'),
(24, 'Implement a simple stack data structure.', 15, 5, 'ESSAY', NULL),
(25, 'What is the difference between an array and a linked list?', 12, 5, 'MCQ', 'Arrays have fixed size, linked lists are dynamic'),
(26, 'Explain tree traversal methods (in-order, pre-order, post-order).', 15, 5, 'ESSAY', NULL),
(27, 'What is the time complexity of finding an element in a balanced BST?', 10, 5, 'MCQ', 'O(log n)');

INSERT INTO mcq_options (question_id, option) VALUES
(23, 'O(1)'), (23, 'O(n)'), (23, 'O(log n)'), (23, 'O(n²)'),
(25, 'Arrays have fixed size, linked lists are dynamic'), (25, 'Linked lists are faster'), (25, 'Arrays use less memory'), (25, 'They are equivalent'),
(27, 'O(log n)'), (27, 'O(1)'), (27, 'O(n)'), (27, 'O(n log n)');

-- Assessment 6: Software Engineering - Case Study (80 points)
INSERT INTO questions (id, content, points, assessment_id, question_type, correct_answer) VALUES
(28, 'What is the main goal of object-oriented design?', 10, 6, 'MCQ', 'Modularity, reusability, and maintainability'),
(29, 'Design a parking lot management system with classes and relationships.', 25, 6, 'ESSAY', NULL),
(30, 'Explain SOLID principles in software design.', 20, 6, 'ESSAY', NULL),
(31, 'What is a design pattern?', 10, 6, 'MCQ', 'A reusable solution to common design problems'),
(32, 'Describe the Model-View-Controller (MVC) architecture.', 15, 6, 'ESSAY', NULL);

INSERT INTO mcq_options (question_id, option) VALUES
(28, 'Modularity, reusability, and maintainability'), (28, 'Fast execution'), (28, 'Easy to learn'), (28, 'Reduces code lines'),
(31, 'A reusable solution to common design problems'), (31, 'A programming language'), (31, 'A software framework'), (31, 'A database schema');

-- Insert sample submissions from 8 students (2 submissions per assessment on average)
INSERT INTO submissions (id, student_id, assessment_id, score, submitted_at) VALUES
(1, 'student01', 1, 22, now() - interval '5 days'),
(2, 'student02', 1, 18, now() - interval '4 days'),
(3, 'student03', 1, 25, now() - interval '3 days'),
(4, 'student01', 2, 75, now() - interval '6 days'),
(5, 'student02', 2, 82, now() - interval '5 days'),
(6, 'student04', 2, 65, now() - interval '4 days'),
(7, 'student05', 3, 38, now() - interval '7 days'),
(8, 'student06', 3, 42, now() - interval '6 days'),
(9, 'student01', 4, 32, now() - interval '2 days'),
(10, 'student03', 4, 28, now() - interval '1 day'),
(11, 'student04', 5, 48, now() - interval '3 days'),
(12, 'student07', 5, 52, now() - interval '2 days'),
(13, 'student08', 6, 64, now() - interval '4 days'),
(14, 'student02', 6, 58, now() - interval '3 days'),
(15, 'student05', 2, 88, now() - interval '1 day');

-- Insert sample answers for submissions
INSERT INTO answers (submission_id, question_id, answer, score) VALUES
-- Submission 1 (student01, Assessment 1)
(1, 1, 'class', 5),
(1, 2, 'String', 5),
(1, 3, 'Good explanation of reference vs value', 8),
(1, 4, 'To create class variables', 5),
-- Submission 2 (student02, Assessment 1)
(2, 1, 'class', 5),
(2, 2, 'String', 5),
(2, 3, 'Brief explanation', 5),
(2, 4, 'To create private methods', 0),
-- Submission 3 (student03, Assessment 1)
(3, 1, 'class', 5),
(3, 2, 'String', 5),
(3, 3, 'Detailed explanation with examples', 10),
(3, 4, 'To create class variables', 5),
-- Submission 4 (student01, Assessment 2)
(4, 6, 'O(log n)', 10),
(4, 7, 'Bubble Sort', 10),
(4, 8, 'Detailed explanation of Dijkstra algorithm', 15),
(4, 9, 'A data structure for fast lookups', 15),
(4, 10, 'Good DP explanation', 20),
(4, 11, 'Heap', 10),
-- Submission 5 (student02, Assessment 2)
(5, 6, 'O(log n)', 10),
(5, 7, 'Bubble Sort', 10),
(5, 8, 'Detailed explanation with examples', 18),
(5, 9, 'A data structure for fast lookups', 15),
(5, 10, 'Excellent DP explanation', 25),
(5, 11, 'Heap', 10),
-- Submission 6 (student04, Assessment 2)
(6, 6, 'O(n)', 0),
(6, 7, 'Bubble Sort', 10),
(6, 8, 'Basic explanation', 12),
(6, 9, 'A data structure for fast lookups', 15),
(6, 10, 'Average explanation', 18),
(6, 11, 'Heap', 10),
-- Submission 7 (student05, Assessment 3)
(7, 13, 'Atomicity, Consistency, Isolation, Durability', 5),
(7, 14, 'Good normalization explanation', 12),
(7, 15, 'A unique identifier for records', 5),
(7, 16, 'Basic schema design', 10),
(7, 17, 'How they filter rows from related tables', 10),
-- Submission 8 (student06, Assessment 3)
(8, 13, 'Atomicity, Consistency, Isolation, Durability', 5),
(8, 14, 'Detailed normalization explanation', 15),
(8, 15, 'A unique identifier for records', 5),
(8, 16, 'Detailed schema design with relationships', 15),
(8, 17, 'How they filter rows from related tables', 10),
-- Submission 9 (student01, Assessment 4)
(9, 18, 'HyperText Markup Language', 5),
(9, 19, 'Good Box Model explanation', 8),
(9, 20, 'margin', 5),
(9, 21, 'Scope and reassignability', 10),
(9, 22, 'Simple form with basic validation', 8),
-- Submission 10 (student03, Assessment 4)
(10, 18, 'HyperText Markup Language', 5),
(10, 19, 'Brief Box Model explanation', 7),
(10, 20, 'margin', 5),
(10, 21, 'Scope and reassignability', 10),
(10, 22, 'Form without validation', 5),
-- Submission 11 (student04, Assessment 5)
(11, 23, 'O(1)', 8),
(11, 24, 'Good stack implementation', 14),
(11, 25, 'Arrays have fixed size, linked lists are dynamic', 12),
(11, 26, 'Good traversal explanation', 14),
-- Submission 12 (student07, Assessment 5)
(12, 23, 'O(1)', 8),
(12, 24, 'Excellent stack implementation', 15),
(12, 25, 'Arrays have fixed size, linked lists are dynamic', 12),
(12, 26, 'Detailed traversal with examples', 15),
-- Submission 13 (student08, Assessment 6)
(13, 28, 'Modularity, reusability, and maintainability', 10),
(13, 29, 'Good parking lot design', 22),
(13, 30, 'Decent SOLID explanation', 18),
(13, 31, 'A reusable solution to common design problems', 10),
(13, 32, 'Basic MVC explanation', 12),
-- Submission 14 (student02, Assessment 6)
(14, 28, 'Modularity, reusability, and maintainability', 10),
(14, 29, 'Detailed parking lot design', 24),
(14, 30, 'Comprehensive SOLID explanation', 20),
(14, 31, 'A reusable solution to common design problems', 10),
(14, 32, 'Detailed MVC explanation', 14),
-- Submission 15 (student05, Assessment 2)
(15, 6, 'O(log n)', 10),
(15, 7, 'Bubble Sort', 10),
(15, 8, 'Excellent Dijkstra explanation with pseudocode', 20),
(15, 9, 'A data structure for fast lookups', 15),
(15, 10, 'Excellent DP explanation with examples', 25),
(15, 11, 'Heap', 10);

-- Sample queries
SELECT * FROM assessments ORDER BY id;

SELECT q.id, q.question_type, q.content, q.points, q.correct_answer,
       ARRAY_AGG(o.option) as options
FROM questions q
LEFT JOIN mcq_options o ON o.question_id = q.id
WHERE q.assessment_id = 1
GROUP BY q.id, q.question_type, q.content, q.points, q.correct_answer
ORDER BY q.id;

SELECT s.id, s.student_id, s.score, s.submitted_at, a.title
FROM submissions s
JOIN assessments a ON a.id = s.assessment_id
ORDER BY s.submitted_at DESC;

SELECT student_id, COUNT(*) as submission_count, AVG(score) as avg_score
FROM submissions
GROUP BY student_id
ORDER BY avg_score DESC;
