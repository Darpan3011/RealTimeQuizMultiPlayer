import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-user-dashboard',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterModule],
    template: `
    <div class="dashboard-container fade-in">
      <header class="dashboard-header">
        <h1>Welcome, {{ currentUser?.email.split('@')[0] }}!</h1>
      </header>

      <div class="card join-card">
        <div class="icon-wrapper">
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <polygon points="5 3 19 12 5 21 5 3"></polygon>
          </svg>
        </div>
        <h2>Join a Quiz</h2>
        <p>Enter the 6-digit access code to join a live game.</p>
        
        <div class="form-group">
          <input type="number" [(ngModel)]="quizCode" placeholder="Quiz Code" (keyup.enter)="joinQuiz()">
        </div>
        
        <button class="btn-primary w-full" (click)="joinQuiz()" [disabled]="!quizCode">
          Join Game
        </button>
      </div>
      
      <div class="trouble-tips text-muted text-center mt-4">
        <p><small>Don't have a code? Ask your quiz administrator.</small></p>
      </div>
    </div>
  `,
    styles: [`
    .dashboard-container {
      max-width: 600px;
      margin: 0 auto;
      padding: 20px;
      min-height: 80vh;
      display: flex;
      flex-direction: column;
      justify-content: center;
    }
    .dashboard-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 40px;
    }
    h1 { margin: 0; font-size: 1.5rem; color: white; }
    
    .card {
      background: white;
      border-radius: 16px;
      padding: 40px;
      text-align: center;
      box-shadow: 0 10px 30px rgba(0,0,0,0.2);
    }
    .icon-wrapper {
      width: 80px;
      height: 80px;
      background: rgba(99, 102, 241, 0.1);
      color: #6366f1;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 20px;
    }
    h2 { margin: 0 0 10px; color: #333; }
    p { margin: 0 0 30px; color: #666; }
    
    .form-group input {
      width: 100%;
      padding: 15px;
      font-size: 1.2rem;
      text-align: center;
      border: 2px solid #e5e7eb;
      border-radius: 12px;
      margin-bottom: 20px;
      letter-spacing: 2px;
      transition: all 0.3s;
    }
    .form-group input:focus {
      outline: none;
      border-color: #6366f1;
    }
    
    .btn-primary {
      background: #6366f1;
      color: white;
      border: none;
      padding: 16px;
      border-radius: 12px;
      font-size: 1.1rem;
      font-weight: 600;
      cursor: pointer;
      width: 100%;
      transition: background 0.3s;
    }
    .btn-primary:hover { background: #4f46e5; }
    .btn-primary:disabled { background: #a5b4fc; cursor: not-allowed; }
    
    .btn-outline {
      background: transparent;
      border: 1px solid rgba(255,255,255,0.3);
      color: white;
      padding: 8px 16px;
      border-radius: 8px;
      cursor: pointer;
    }
    .btn-outline:hover { background: rgba(255,255,255,0.1); }
    
    .text-muted { color: rgba(255,255,255,0.6); }
    
    .fade-in { animation: fadeIn 0.5s ease-out; }
    @keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
  `]
})
export class UserDashboardComponent {
    currentUser: any;
    quizCode: number | null = null;

    constructor(private authService: AuthService, private router: Router) {
        this.currentUser = this.authService.getCurrentUser();
    }

    joinQuiz() {
        if (this.quizCode) {
            const playerName = this.currentUser?.email.split('@')[0] || 'Unknown';
            this.router.navigate(['/game', this.quizCode], { queryParams: { player: playerName } });
        }
    }

    logout() {
        this.authService.logout();
    }
}
