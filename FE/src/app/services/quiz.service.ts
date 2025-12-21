import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class QuizService {
  private apiUrl = 'http://localhost:8081';

  constructor(private http: HttpClient) { }

  createQuiz(quiz: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/quiz`, quiz);
  }

  addQuestion(question: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/question`, question);
  }

  getQuizByCode(code: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/quiz/code/${code}`);
  }

  getLeaderboard(quizCode: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/quiz/leaderboard/${quizCode}`);
  }
}
