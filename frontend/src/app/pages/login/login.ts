import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NgIf, isPlatformBrowser } from '@angular/common';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { AuthService } from '../../auth/auth.service';
import { RoleService } from '../../auth/role.service';
import { STORAGE_KEYS } from '../../app.constants';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink, NgIf, MatSnackBarModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  loginData = { email: '', password: '', role: 'USER' };
  error = '';
  loading = false;
  private isBrowser: boolean;

  constructor(
    private auth: AuthService,
    private roles: RoleService,
    private router: Router,
    private snackBar: MatSnackBar,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  login(): void {
    this.error = '';
    this.loading = true;

    this.auth.login(this.loginData).subscribe({
      next: (res: any) => {
        console.log('LOGIN RESPONSE:', res); // debug — remove later

        this.loading = false;
        this.auth.saveToken(res);

        const role = (res?.role ?? res?.user?.role ?? this.loginData.role ?? 'USER').toUpperCase();
        const userId = res?.id ?? res?.userId ?? res?.user?.id;

        // ✅ Safe localStorage — only in browser
        if (this.isBrowser) {
          localStorage.setItem(STORAGE_KEYS.userRole, role);
          if (userId !== undefined && userId !== null) {
            localStorage.setItem(STORAGE_KEYS.userId, String(userId));
          }
        }

        const target =
          role === 'ADMIN'
            ? '/admin-dashboard'
            : role === 'RECRUITER'
            ? '/recruiter-dashboard'
            : '/dashboard';

        this.snackBar.open('Login successful!', 'Close', {
          duration: 2000,
          horizontalPosition: 'right',
          verticalPosition: 'bottom'
        });

        setTimeout(() => this.router.navigate([target]), 500);
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message || 'Login failed. Check your credentials.';
      }
    });
  }
}
