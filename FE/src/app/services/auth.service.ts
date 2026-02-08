import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs'; // Removed 'throwError'
import { Router } from '@angular/router';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = 'http://localhost:8081/api/auth';
    private userSubject = new BehaviorSubject<any>(null);
    public user$ = this.userSubject.asObservable();

    constructor(private http: HttpClient, private router: Router) {
        this.loadUserFromStorage();
    }

    // Load user from local storage on app start
    private loadUserFromStorage() {
        const token = localStorage.getItem('accessToken');
        if (token) {
            // Decode token to get user info (simplified for now)
            const payload = JSON.parse(atob(token.split('.')[1]));
            this.userSubject.next({ email: payload.sub, role: payload.role });
        }
    }

    register(user: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/register`, user);
    }

    login(credentials: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
            tap((res: any) => {
                if (!res.mfaRequired && res.accessToken) {
                    this.setSession(res);
                }
            })
        );
    }

    verifyMfa(data: any): Observable<any> {
        return this.http.post(`${this.apiUrl}/verify-mfa`, data).pipe(
            tap((res: any) => {
                this.setSession(res);
            })
        );
    }

    logout() {
        const refreshToken = localStorage.getItem('refreshToken');
        if (refreshToken) {
            this.http.post(`${this.apiUrl}/logout`, { refreshToken }).subscribe();
        }
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        this.userSubject.next(null);
        this.router.navigate(['/login']);
    }

    private setSession(authResult: any) {
        localStorage.setItem('accessToken', authResult.accessToken);
        localStorage.setItem('refreshToken', authResult.refreshToken);
        // Decode token
        const payload = JSON.parse(atob(authResult.accessToken.split('.')[1]));
        this.userSubject.next({ email: payload.sub, role: payload.role });
    }

    getToken(): string | null {
        return localStorage.getItem('accessToken');
    }

    isAuthenticated(): boolean {
        return !!this.getToken();
    }

    getCurrentUser(): any {
        return this.userSubject.value;
    }

    hasRole(role: string): boolean {
        const user = this.getCurrentUser();
        return user && user.role === role;
    }

    verifyEmail(token: string): Observable<any> {
        return this.http.post(`${this.apiUrl}/verify-email`, null, { params: { token } });
    }

    enableMfa(email: string): Observable<any> {
        return this.http.put(`${this.apiUrl}/mfa/enable`, null, { params: { email } });
    }

    disableMfa(email: string): Observable<any> {
        return this.http.put(`${this.apiUrl}/mfa/disable`, null, { params: { email } });
    }
}
