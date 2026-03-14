import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { STORAGE_KEYS } from '../app.constants';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = 'http://localhost:8082';
  private isBrowser: boolean;

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  private getItem(key: string): string | null {
    return this.isBrowser ? localStorage.getItem(key) : null;
  }
  private setItem(key: string, value: string): void {
    if (this.isBrowser) localStorage.setItem(key, value);
  }
  private removeItem(key: string): void {
    if (this.isBrowser) localStorage.removeItem(key);
  }

  // BEFORE:


// AFTER:
login(credentials: { email: string; password: string; role?: string }): Observable<any> {
  return this.http.post(`${this.baseUrl}/api/v1/auth/login`, {
    email: credentials.email,
    password: credentials.password
  });
}
  register(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/api/v1/auth/register`, data);
  }
  saveToken(response: any): void {
    if (response?.token) this.setItem(STORAGE_KEYS.token, response.token);
    if (response?.email) this.setItem(STORAGE_KEYS.userEmail, response.email);
  }
  getToken(): string | null { return this.getItem(STORAGE_KEYS.token); }
  isLoggedIn(): boolean { return !!this.getToken(); }
  getUserId(): string | null { return this.getItem(STORAGE_KEYS.userId); }
  getUserRole(): string | null { return this.getItem(STORAGE_KEYS.userRole); }
  getUserEmail(): string | null { return this.getItem(STORAGE_KEYS.userEmail); }
  logout(): void {
    this.removeItem(STORAGE_KEYS.token);
    this.removeItem(STORAGE_KEYS.userId);
    this.removeItem(STORAGE_KEYS.userRole);
    this.removeItem(STORAGE_KEYS.userEmail);
    this.router.navigate(['/login']);
  }
}