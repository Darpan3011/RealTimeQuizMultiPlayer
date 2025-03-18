# Real-Time Quiz Management System

## Project Overview

This is a real-time quiz management system where users can:
- Create an account
- Create quizzes and set a unique quiz code
- Join a quiz using the quiz code
- View and answer quiz questions

## Features

- User authentication (account creation & login)
- Quiz creation and unique quiz code generation
- Players can join quizzes using a quiz code
- Real-time question display and answer submission
- Score tracking

## Database Schema

The project includes the following tables:

### 1. `player`
Stores player information.

| Column  | Description        |
|---------|--------------------|
| `id`    | Unique player ID   |
| `name`  | Player's name      |
| `score` | Player's score     |

### 2. `player_quiz`
Maps players to quizzes.

| Column      | Description                 |
|------------|-----------------------------|
| `player_id` | References `player.id`     |
| `quiz_id`   | References `quiz.quiz_id`  |

### 3. `quiz`
Stores quiz details.

| Column      | Description              |
|------------|--------------------------|
| `quiz_id`   | Unique quiz ID           |
| `quiz_code` | Unique code for the quiz |
| `quiz_name` | Quiz name                |

### 4. `question`
Stores quiz questions.

| Column          | Description              |
|---------------|--------------------------|
| `question_id`  | Unique question ID       |
| `question_title` | Question text           |
| `correct_answer` | Correct answer         |
| `quiz_id`      | References `quiz.quiz_id` |

### 5. `question_options`
Stores possible answers for each question.

| Column                | Description                     |
|----------------------|---------------------------------|
| `question_question_id` | References `question.question_id` |
| `options`            | Answer options                  |

### Prerequisites
- Java Springboot
- MySQL