import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { GameComponent } from './components/game/game.component';
import { AdminComponent } from './components/admin/admin.component';

export const routes: Routes = [
    { path: '', component: LoginComponent },
    { path: 'game/:code', component: GameComponent },
    { path: 'admin', component: AdminComponent },
];
