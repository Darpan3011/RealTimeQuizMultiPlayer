import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { QuizService } from '../../services/quiz.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss'
})
export class AdminComponent {
  activeTab: any = 'create';
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
  correctAnswerIndex: number | null = null;

  // View Results
  resultsQuizCode: number | null = null;
  leaderboardData: any[] = [];
  showingResults: boolean = false;

  // Settings
  mfaEnabled: boolean = false;
  currentUser: any = null;

  message: string = '';

  constructor(private quizService: QuizService, private authService: AuthService) {
    this.currentUser = this.authService.getCurrentUser();
    // In a real app, you'd fetch the actual MFA status from backend
    // For now we'll assume it matches the user's initial state or default to false
  }

  selectTab(tab: any) {
    this.activeTab = tab;
    this.message = '';
    this.showingResults = false;

    if (tab === 'settings') {
      // Refresh user data to get latest MFA status
      this.currentUser = this.authService.getCurrentUser();
    }
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
    if (!this.targetQuizCode || !this.questionTitle || this.correctAnswerIndex === null) return;

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
          correctAnswerIndex: this.correctAnswerIndex,
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
            this.correctAnswerIndex = null;

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

  viewResults() {
    if (!this.resultsQuizCode) return;

    this.quizService.getLeaderboard(this.resultsQuizCode).subscribe({
      next: (data) => {
        this.leaderboardData = data;
        this.showingResults = true;
        if (data.length === 0) {
          this.statusType = 'error';
          this.message = 'No results found for this quiz code';
          setTimeout(() => this.message = '', 3000);
        }
      },
      error: (err) => {
        this.statusType = 'error';
        this.message = 'Quiz not found or no results available';
        this.showingResults = false;
        setTimeout(() => this.message = '', 3000);
      }
    });
  }

  toggleMfa() {
    if (!this.currentUser) return;

    // We're toggling, so if it's currently enabled, we disable it, and vice versa
    // But since we're using a checkbox bound to mfaEnabled, the value is already the new desired state
    // Actually, let's implement explicit buttons for better UX or handle the toggle

    const email = this.currentUser.email;
    const action = this.mfaEnabled ? this.authService.enableMfa(email) : this.authService.disableMfa(email);

    action.subscribe({
      next: (res: any) => {
        this.statusType = 'success';
        this.message = res.message;
        setTimeout(() => this.message = '', 3000);
      },
      error: (err: any) => {
        this.statusType = 'error';
        this.message = err.error?.message || 'Failed to update MFA settings';
        this.mfaEnabled = !this.mfaEnabled; // Revert change on error
        setTimeout(() => this.message = '', 3000);
      }
    });
  }

  logout() {
    this.authService.logout();
  }
}
