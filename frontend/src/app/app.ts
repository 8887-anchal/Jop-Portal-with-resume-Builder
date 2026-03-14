import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NgIf } from '@angular/common';

import { AuthService } from './auth/auth.service';
import { RoleService } from './auth/role.service';
import { NavbarComponent } from './components/navbar/navbar';
import { SidebarComponent } from './components/sidebar/sidebar';
import { FooterComponent } from './components/footer/footer';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NgIf, NavbarComponent, SidebarComponent, FooterComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private auth = inject(AuthService);
  private roles = inject(RoleService);

  title = 'Job Portal';

  isLoggedIn(): boolean {
    return this.auth.isLoggedIn();
  }

  isRecruiter(): boolean {
    return this.roles.getRole() === 'RECRUITER';
  }

  isAdmin(): boolean {
    return this.roles.getRole() === 'ADMIN';
  }

  getRole(): string | null {
    return this.roles.getRole();
  }

  logout(): void {
    this.auth.logout();
  }
}

