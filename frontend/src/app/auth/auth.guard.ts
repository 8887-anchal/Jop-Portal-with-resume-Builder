import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = () => {
  const auth: AuthService = inject(AuthService);   // ✅ explicitly typed — fixes ts(18046)
  const router: Router = inject(Router);

  return auth.isLoggedIn() ? true : router.createUrlTree(['/login']);
};