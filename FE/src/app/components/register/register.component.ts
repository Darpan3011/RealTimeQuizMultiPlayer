import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterModule],
    template: `
    <div class="auth-container fade-in">
      <div class="auth-card">
        <div class="auth-header">
          <h2>Create Account</h2>
          <p>Join the quiz platform today</p>
        </div>

        <div *ngIf="successMessage" class="alert success">
          {{ successMessage }}
        </div>

        <div *ngIf="errorMessage" class="alert error">
          {{ errorMessage }}
        </div>

        <form [formGroup]="registerForm" (ngSubmit)="onSubmit()" *ngIf="!successMessage">
          <div class="form-row">
            <div class="form-group half">
              <label for="firstName">First Name</label>
              <input type="text" id="firstName" formControlName="firstName" placeholder="John">
              <div class="error-msg" *ngIf="f['firstName'].touched && f['firstName'].errors?.['required']">
                First name is required
              </div>
            </div>
            <div class="form-group half">
              <label for="lastName">Last Name</label>
              <input type="text" id="lastName" formControlName="lastName" placeholder="Doe">
              <div class="error-msg" *ngIf="f['lastName'].touched && f['lastName'].errors?.['required']">
                Last name is required
              </div>
            </div>
          </div>

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
            <div class="error-msg" *ngIf="f['password'].touched && f['password'].errors?.['minlength']">
              Password must be at least 6 characters
            </div>
          </div>

          <button type="submit" [disabled]="registerForm.invalid || isLoading" class="btn-primary full-width">
            {{ isLoading ? 'Creating Account...' : 'Sign Up' }}
          </button>
        </form>

        <div class="auth-footer">
          <p>Already have an account? <a routerLink="/login">Log In</a></p>
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
    .form-row {
      display: flex;
      gap: 15px;
    }
    .form-group.half {
      flex: 1;
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
export class RegisterComponent {
    registerForm: FormGroup;
    isLoading = false;
    showPassword = false;
    successMessage = '';
    errorMessage = '';

    constructor(
        private fb: FormBuilder,
        private authService: AuthService
    ) {
        this.registerForm = this.fb.group({
            firstName: ['', Validators.required],
            lastName: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    get f() { return this.registerForm.controls; }

    togglePassword() {
        this.showPassword = !this.showPassword;
    }

    onSubmit() {
        if (this.registerForm.invalid) return;

        this.isLoading = true;
        this.errorMessage = '';

        this.authService.register(this.registerForm.value).subscribe({
            next: (res) => {
                this.isLoading = false;
                this.successMessage = res.message;
                this.registerForm.reset();
            },
            error: (err) => {
                this.isLoading = false;
                this.errorMessage = err.error?.message || 'Registration failed. Please try again.';
            }
        });
    }
}
