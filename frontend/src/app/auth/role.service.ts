import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { STORAGE_KEYS } from '../app.constants';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private isBrowser: boolean;

  constructor(@Inject(PLATFORM_ID) platformId: Object) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  getRole(): string | null {
    if (!this.isBrowser) return null;
    return localStorage.getItem(STORAGE_KEYS.userRole);
  }

  setRole(role: string): void {
    if (!this.isBrowser) return;
    localStorage.setItem(STORAGE_KEYS.userRole, role);
  }

  clearRole(): void {
    if (!this.isBrowser) return;
    localStorage.removeItem(STORAGE_KEYS.userRole);
  }

  isRecruiter(): boolean {
    return this.getRole() === 'RECRUITER';
  }

  isAdmin(): boolean {
    return this.getRole() === 'ADMIN';
  }

  isUser(): boolean {
    return this.getRole() === 'USER';
  }
}