import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  playerName: string = '';
  quizCode: string = '';
  message: string = '';

  constructor(private router: Router) { }

  joinGame() {
    if (this.playerName && this.quizCode) {
      this.router.navigate(['/game', this.quizCode], { queryParams: { player: this.playerName } });
    }
  }
}
