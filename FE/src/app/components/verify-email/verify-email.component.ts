import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-verify-email',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="auth-container fade-in">
      <div class="auth-card text-center">
        <div class="icon-wrapper" [class.success]="isSuccess" [class.error]="isError" *ngIf="!isLoading">
          <svg *ngIf="isSuccess" xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
            <polyline points="22 4 12 14.01 9 11.01"></polyline>
          </svg>
          <svg *ngIf="isError" xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="10"></circle>
            <line x1="12" y1="8" x2="12" y2="12"></line>
            <line x1="12" y1="16" x2="12.01" y2="16"></line>
          </svg>
        </div>

        <div *ngIf="isLoading" class="loader-wrapper">
          <div class="spinner"></div>
          <p>Verifying your email...</p>
        </div>

        <div *ngIf="!isLoading">
          <h2>{{ statusTitle }}</h2>
          <p class="message">{{ statusMessage }}</p>
          
          <div class="actions">
            <a routerLink="/login" class="btn-primary">Go to Login</a>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .auth-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 80vh;
      padding: 20px;
    }
    .auth-card {
      background: white;
      border-radius: 12px;
      padding: 40px;
      width: 100%;
      max-width: 480px;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      text-align: center;
    }
    .icon-wrapper {
      width: 80px;
      height: 80px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 20px;
    }
    .icon-wrapper.success {
      background: #ecfdf5;
      color: #059669;
    }
    .icon-wrapper.error {
      background: #fef2f2;
      color: #dc2626;
    }
    h2 {
      color: #333;
      margin-bottom: 10px;
    }
    .message {
      color: #666;
      margin-bottom: 30px;
      line-height: 1.5;
    }
    .btn-primary {
      display: inline-block;
      background: #6366f1;
      color: white;
      border: none;
      padding: 12px 30px;
      border-radius: 8px;
      font-size: 16px;
      font-weight: 600;
      cursor: pointer;
      text-decoration: none;
      transition: background 0.3s;
    }
    .btn-primary:hover {
      background: #4f46e5;
    }
    .loader-wrapper {
      padding: 20px;
    }
    .spinner {
      width: 40px;
      height: 40px;
      border: 4px solid #f3f3f3;
      border-top: 4px solid #6366f1;
      border-radius: 50%;
      margin: 0 auto 15px;
      animation: spin 1s linear infinite;
    }
    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }
    .fade-in {
      animation: fadeIn 0.5s ease-out;
    }
    @keyframes fadeIn {
      from { opacity: 0; transform: translateY(10px); }
      to { opacity: 1; transform: translateY(0); }
    }
  `]
})
export class VerifyEmailComponent implements OnInit {
  isLoading = true;
  isSuccess = false;
  isError = false;
  statusTitle = 'Verifying...';
  statusMessage = 'Please wait while we verify your email address.';

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService
  ) { }

  ngOnInit() {
    const token = this.route.snapshot.queryParamMap.get('token');

    if (!token) {
      this.setError('Invlid Link', 'No verification token found. Please check your email link.');
      return;
    }

    this.authService.verifyEmail(token).subscribe({
      next: (res: any) => {
        this.setSuccess(res.message);
      },
      error: (err: any) => {
        this.setError('Verification Failed', err.error?.message || 'The verification link is invalid or has expired.');
      }
    });
  }

  private setSuccess(message: string) {
    this.isLoading = false;
    this.isSuccess = true;
    this.statusTitle = 'Email Verified!';
    this.statusMessage = message;
  }

  private setError(title: string, message: string) {
    this.isLoading = false;
    this.isError = true;
    this.statusTitle = title;
    this.statusMessage = message;
  }
}
