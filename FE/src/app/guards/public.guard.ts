import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
    providedIn: 'root'
})
export class PublicGuard implements CanActivate {
    constructor(private authService: AuthService, private router: Router) { }

    canActivate(): boolean | UrlTree {
        if (this.authService.isAuthenticated()) {
            // Already logged in, redirect based on role
            if (this.authService.hasRole('ADMIN')) {
                return this.router.createUrlTree(['/admin']);
            } else {
                return this.router.createUrlTree(['/dashboard']);
            }
        }
        return true; // Allow access to login/register if not logged in
    }
}
