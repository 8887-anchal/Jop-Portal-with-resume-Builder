import { Routes } from '@angular/router';

import { LoginComponent } from './pages/login/login';
import { RegisterComponent } from './pages/register/register';
import { DashboardComponent } from './pages/dashboard/dashboard';
import { ResumeBuilderComponent } from './pages/resume-builder/resume-builder';
import { JobListComponent } from './pages/job-list/job-list';
import { JobDetailsComponent } from './pages/job-details/job-details';
import { RecruiterDashboardComponent } from './pages/recruiter-dashboard/recruiter-dashboard';
import { AdminDashboardComponent } from './pages/admin-dashboard/admin-dashboard';
import { ApplicationsComponent } from './pages/applications/applications';
import { authGuard } from './auth/auth.guard';
import { recruiterGuard, adminGuard } from './auth/role.guard';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'resume-builder', component: ResumeBuilderComponent, canActivate: [authGuard] },
  { path: 'jobs', component: JobListComponent, canActivate: [authGuard] },
  { path: 'job-list', redirectTo: 'jobs', pathMatch: 'full' },
  { path: 'job-details/:id', component: JobDetailsComponent, canActivate: [authGuard] },
  { path: 'job-details', redirectTo: 'jobs', pathMatch: 'full' },
  { path: 'applications', component: ApplicationsComponent, canActivate: [authGuard] },
  { path: 'recruiter-dashboard', component: RecruiterDashboardComponent, canActivate: [authGuard, recruiterGuard] },
  { path: 'admin-dashboard', component: AdminDashboardComponent, canActivate: [authGuard, adminGuard] },
  { path: '**', redirectTo: '' }
];

