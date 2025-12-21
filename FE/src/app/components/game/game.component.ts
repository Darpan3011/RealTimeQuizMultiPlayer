import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { WebSocketService } from '../../services/web-socket.service';
import { QuizService } from '../../services/quiz.service';

interface Question {
  questionTitle: string;
  options: string[];
  correctAnswer?: string; // Hidden in real scenarios, mainly for self-check if leak
}

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './game.component.html',
  styleUrl: './game.component.scss'
})
export class GameComponent implements OnInit, OnDestroy {
  quizCode: string = '';
  playerName: string = '';

  questions: createQuestion[] = [];
  currentQuestionIndex: number = 0;
  userAnswers: string[] = [];

  leaderboard: any[] = [];
  gameFinished: boolean = false;
  loading: boolean = true;

  constructor(
    private route: ActivatedRoute,
    private wsService: WebSocketService,
    private quizService: QuizService
  ) { }

  ngOnInit() {
    this.quizCode = this.route.snapshot.paramMap.get('code') || '';
    this.playerName = this.route.snapshot.queryParamMap.get('player') || 'Anonymous';

    if (this.quizCode) {
      this.setupSubscriptions();
      this.loadLeaderboard(); // Load initial leaderboard
    }
  }

  loadLeaderboard() {
    this.quizService.getLeaderboard(parseInt(this.quizCode)).subscribe({
      next: (lb) => {
        this.leaderboard = lb;
      },
      error: (err) => {
        console.error('Error loading leaderboard:', err);
      }
    });
  }

  setupSubscriptions() {
    // Subscribe to Quiz Topic (Questions & Results)
    this.wsService.subscribe(`/topic/quiz/${this.quizCode}`, (msg: any) => {
      console.log('Quiz Msg:', msg);
      if (Array.isArray(msg)) {
        // It's the questions list
        this.questions = msg;
        this.loading = false;
        // Initialize answers array
        this.userAnswers = new Array(this.questions.length).fill(null);
      } else {
        // It's a text message (e.g. "Player X Score: ...") or Error
        // specific logic if needed, e.g. snackbar
      }
    });

    // Subscribe to Leaderboard Topic
    this.wsService.subscribe(`/topic/leaderboard/${this.quizCode}`, (lb: any[]) => {
      console.log('Leaderboard Update:', lb);
      this.leaderboard = lb;
    });

    // Trigger fetch whenever connected (handles reconnects too)
    this.wsService.getConnectionStatus().subscribe(connected => {
      if (connected) {
        console.log('Connected! Sending fetch request for quiz:', this.quizCode);
        this.wsService.send(`/rtquiz/fetch/${this.quizCode}`, {});
      }
    });
  }

  selectOption(option: string) {
    this.userAnswers[this.currentQuestionIndex] = option;
  }

  nextQuestion() {
    if (this.currentQuestionIndex < this.questions.length - 1) {
      this.currentQuestionIndex++;
    } else {
      this.submitQuiz();
    }
  }

  submitQuiz() {
    this.gameFinished = true;
    this.wsService.send(`/rtquiz/submit/${this.quizCode}/${this.playerName}`, this.userAnswers);
  }

  ngOnDestroy() {
    // Cleanup handled by service or connection close usually, 
    // but ideally we unsubscribe. For MVP we skip explicit unsubscribe 
    // as Service manages connection singleton.
  }
}

// Helper interface
interface createQuestion {
  questionTitle: string;
  options: string[];
}
