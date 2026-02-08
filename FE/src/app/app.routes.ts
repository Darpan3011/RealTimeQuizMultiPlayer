import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { VerifyEmailComponent } from './components/verify-email/verify-email.component';
import { GameComponent } from './components/game/game.component';
import { AdminComponent } from './components/admin/admin.component';
import { UserDashboardComponent } from './components/user-dashboard/user-dashboard.component';
import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';
import { PublicGuard } from './guards/public.guard';

export const routes: Routes = [
    { path: 'login', component: LoginComponent, canActivate: [PublicGuard] },
    { path: 'register', component: RegisterComponent, canActivate: [PublicGuard] },
    { path: 'verify-email', component: VerifyEmailComponent }, // Public
    { path: 'dashboard', component: UserDashboardComponent, canActivate: [AuthGuard] },
    { path: 'game/:code', component: GameComponent, canActivate: [AuthGuard] },
    { path: 'admin', component: AdminComponent, canActivate: [AdminGuard] },
    { path: '', redirectTo: '/login', pathMatch: 'full' } // PublicGuard on login will handle redirect if logged in
];
