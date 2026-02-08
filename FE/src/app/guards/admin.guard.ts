import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AdminGuard implements CanActivate {
    constructor(private authService: AuthService, private router: Router) { }

    canActivate(): boolean | UrlTree {
        if (this.authService.isAuthenticated() && this.authService.hasRole('ADMIN')) {
            return true;
        }
        // If authenticated but not admin, go to dashboard
        if (this.authService.isAuthenticated()) {
            return this.router.createUrlTree(['/dashboard']);
        }
        // If not authenticated, go to login
        return this.router.createUrlTree(['/login']);
    }
}
