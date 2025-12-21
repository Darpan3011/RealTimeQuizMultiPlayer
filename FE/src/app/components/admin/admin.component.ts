import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { QuizService } from '../../services/quiz.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss'
})
export class AdminComponent {
  activeTab: 'create' | 'add' = 'create';
  statusType: 'success' | 'error' = 'success';
  // Create Quiz
  quizName: string = '';
  quizCode: number | null = null;

  // Add Question
  targetQuizCode: number | null = null;
  questionTitle: string = '';
  option1: string = '';
  option2: string = '';
  option3: string = '';
  option4: string = '';
  correctAnswer: string = '';

  message: string = '';

  constructor(private quizService: QuizService) { }

  selectTab(tab: 'create' | 'add') {
    this.activeTab = tab;
    this.message = '';
  }

  createQuiz() {
    if (!this.quizCode || !this.quizName) return;

    const quizData = {
      quizName: this.quizName,
      quizCode: this.quizCode
    };

    this.quizService.createQuiz(quizData).subscribe({
      next: (res) => {
        this.statusType = 'success';
        this.message = 'Quiz Created Successfully!';
        this.targetQuizCode = this.quizCode; // Auto-fill for questions

        // Auto-clear message after 3 seconds
        setTimeout(() => this.message = '', 4000);
      },
      error: (err) => {
        this.statusType = 'error';
        this.message = 'Error creating quiz: ' + (err.error?.message || err.message);
        setTimeout(() => this.message = '', 5000);
      }
    });
  }

  addQuestion() {
    if (!this.targetQuizCode || !this.questionTitle || !this.correctAnswer) return;

    // First get the Quiz ID (internal) from the code, or if backend handles code directly
    // The previous backend implementation of QuestionDTO expects `quizId` (internal database ID).
    // But my Admin HTML implementation previously fetched the ID first.
    // Let's check if the backend QuestionDTO uses quizId or quizCode.
    // Wait, the backend QuestionDTO was: private int quizId; 
    // And QuizServiceImpl.createQuestion uses quizRepository.findById(questionDTO.getQuizId())
    // So we MUST send "quizId" (DB ID), not "quizCode".

    // PROBLEM: The user enters "Code" (e.g. 100). We need the ID (e.g. 1).
    // WE NEED TO FETCH THE QUIZ OBJECT FIRST to get the ID.

    this.quizService.getQuizByCode(this.targetQuizCode).subscribe({
      next: (quiz) => {
        const questionData = {
          questionTitle: this.questionTitle,
          options: [this.option1, this.option2, this.option3, this.option4],
          correctAnswer: this.correctAnswer,
          quizId: quiz.quizId  // Backend QuizDTO has 'quizId' field
        };

        this.quizService.addQuestion(questionData).subscribe({
          next: () => {
            this.statusType = 'success';
            this.message = 'Question Added!';
            // Clear form
            this.questionTitle = '';
            this.option1 = '';
            this.option2 = '';
            this.option3 = '';
            this.option4 = '';
            this.correctAnswer = '';

            // Auto-clear message after 3 seconds
            setTimeout(() => this.message = '', 3000);
          },
          error: (err) => {
            this.statusType = 'error';
            this.message = 'Error adding question: ' + (err.error?.message || err.message);
            setTimeout(() => this.message = '', 5000);
          }
        });
      },
      error: (err) => {
        this.statusType = 'error';
        this.message = 'Quiz Code not found';
        setTimeout(() => this.message = '', 5000);
      }
    });
  }
}
