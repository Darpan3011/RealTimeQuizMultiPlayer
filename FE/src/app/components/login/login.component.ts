import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterModule],
  template: `
    <div class="auth-container fade-in">
      <div class="auth-card">
        <div class="auth-header">
          <h2>{{ mfaRequired ? 'Two-Factor Authentication' : 'Welcome Back' }}</h2>
          <p>{{ mfaRequired ? 'Enter the code sent to your email' : 'Sign in to your account' }}</p>
        </div>

        <div *ngIf="errorMessage" class="alert error">
          {{ errorMessage }}
        </div>
        <div *ngIf="successMessage" class="alert success">
          {{ successMessage }}
        </div>

        <!-- Login Form -->
        <form [formGroup]="loginForm" (ngSubmit)="onLogin()" *ngIf="!mfaRequired">
          <div class="form-group">
            <label for="email">Email Address</label>
            <input type="email" id="email" formControlName="email" placeholder="john@example.com">
            <div class="error-msg" *ngIf="f['email'].touched && f['email'].errors?.['required']">
              Email is required
            </div>
            <div class="error-msg" *ngIf="f['email'].touched && f['email'].errors?.['email']">
              Please enter a valid email
            </div>
          </div>

          <div class="form-group">
            <label for="password">Password</label>
            <div class="password-input">
              <input [type]="showPassword ? 'text' : 'password'" id="password" formControlName="password" placeholder="••••••••">
              <button type="button" class="toggle-password" (click)="togglePassword()">
                {{ showPassword ? 'Hide' : 'Show' }}
              </button>
            </div>
            <div class="error-msg" *ngIf="f['password'].touched && f['password'].errors?.['required']">
              Password is required
            </div>
          </div>

          <button type="submit" [disabled]="loginForm.invalid || isLoading" class="btn-primary full-width">
            {{ isLoading ? 'Signing In...' : 'Sign In' }}
          </button>
        </form>

        <!-- MFA Form -->
        <div *ngIf="mfaRequired" class="mfa-section">
          <div class="form-group">
            <label for="mfaCode">Verification Code</label>
            <input type="text" id="mfaCode" [(ngModel)]="mfaCode" placeholder="123456" maxlength="6" class="code-input">
            <p class="hint">Check your email for the 6-digit code</p>
          </div>

          <button (click)="verifyMfa()" [disabled]="!mfaCode || mfaCode.length < 6 || isLoading" class="btn-primary full-width">
            {{ isLoading ? 'Verifying...' : 'Verify Code' }}
          </button>
          
          <button (click)="cancelMfa()" class="btn-text full-width">Back to Login</button>
        </div>

        <div class="auth-footer" *ngIf="!mfaRequired">
          <p>Don't have an account? <a routerLink="/register">Sign Up</a></p>
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
    }
    .auth-header {
      text-align: center;
      margin-bottom: 30px;
    }
    .auth-header h2 {
      margin: 0 0 8px;
      color: #333;
    }
    .auth-header p {
      margin: 0;
      color: #666;
    }
    .form-group {
      margin-bottom: 20px;
    }
    label {
      display: block;
      margin-bottom: 8px;
      font-weight: 500;
      color: #444;
    }
    input {
      width: 100%;
      padding: 12px;
      border: 1px solid #ddd;
      border-radius: 8px;
      font-size: 15px;
      transition: border-color 0.3s;
    }
    input:focus {
      outline: none;
      border-color: #6366f1;
    }
    .password-input {
      position: relative;
    }
    .toggle-password {
      position: absolute;
      right: 12px;
      top: 50%;
      transform: translateY(-50%);
      background: none;
      border: none;
      color: #666;
      cursor: pointer;
      font-size: 13px;
    }
    .code-input {
      font-size: 24px;
      letter-spacing: 5px;
      text-align: center;
    }
    .hint {
      font-size: 13px;
      color: #666;
      margin-top: 5px;
    }
    .error-msg {
      color: #ef4444;
      font-size: 13px;
      margin-top: 5px;
    }
    .btn-primary {
      background: #6366f1;
      color: white;
      border: none;
      padding: 14px;
      border-radius: 8px;
      font-size: 16px;
      font-weight: 600;
      cursor: pointer;
      transition: background 0.3s;
    }
    .btn-primary:hover {
      background: #4f46e5;
    }
    .btn-primary:disabled {
      background: #a5b4fc;
      cursor: not-allowed;
    }
    .btn-text {
      background: none;
      border: none;
      color: #666;
      padding: 10px;
      margin-top: 10px;
      cursor: pointer;
      font-size: 14px;
    }
    .btn-text:hover {
      color: #333;
    }
    .full-width {
      width: 100%;
    }
    .alert {
      padding: 15px;
      border-radius: 8px;
      margin-bottom: 20px;
    }
    .success {
      background: #ecfdf5;
      color: #065f46;
      border: 1px solid #a7f3d0;
    }
    .error {
      background: #fef2f2;
      color: #991b1b;
      border: 1px solid #fecaca;
    }
    .auth-footer {
      text-align: center;
      margin-top: 20px;
      font-size: 14px;
      color: #666;
    }
    .auth-footer a {
      color: #6366f1;
      text-decoration: none;
      font-weight: 500;
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
export class LoginComponent {
  loginForm: FormGroup;
  isLoading = false;
  showPassword = false;
  errorMessage = '';
  successMessage = '';

  // MFA State
  mfaRequired = false;
  mfaCode = '';
  tempEmail = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  get f() { return this.loginForm.controls; }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  onLogin() {
    if (this.loginForm.invalid) return;

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.login(this.loginForm.value).subscribe({
      next: (res) => {
        this.isLoading = false;

        if (res.mfaRequired) {
          this.mfaRequired = true;
          this.tempEmail = this.f['email'].value;
          this.successMessage = res.message;
        } else {
          // Check role and redirect
          const user = this.authService.getCurrentUser();
          if (user.role === 'ADMIN') {
            this.router.navigate(['/admin']);
          } else {
            this.router.navigate(['/dashboard']);
          }
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Login failed. Please check your credentials.';
      }
    });
  }

  verifyMfa() {
    if (!this.mfaCode) return;

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const verifyData = {
      email: this.tempEmail,
      code: this.mfaCode
    };

    this.authService.verifyMfa(verifyData).subscribe({
      next: (res) => {
        this.isLoading = false;
        const user = this.authService.getCurrentUser();
        if (user.role === 'ADMIN') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/dashboard']);
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Verification failed. Invalid code.';
      }
    });
  }

  cancelMfa() {
    this.mfaRequired = false;
    this.mfaCode = '';
    this.tempEmail = '';
    this.successMessage = '';
    this.errorMessage = '';
  }
}
