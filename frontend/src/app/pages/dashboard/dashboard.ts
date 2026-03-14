import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgFor, NgIf, SlicePipe } from '@angular/common';

import { JobsService } from '../../services/job.service';
import { Job } from '../../models/job';
import { NotificationsService, NotificationItem } from '../../services/notification.service';
import { ApplicationsService, JobApplication } from '../../services/application.service';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink, NgIf, NgFor, SlicePipe],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent {
  jobs: Job[] = [];
  notifications: NotificationItem[] = [];
  applications: JobApplication[] = [];

  constructor(
    private jobsService: JobsService,
    private notificationsService: NotificationsService,
    private applicationsService: ApplicationsService,
    private auth: AuthService
  ) {
    this.loadJobs();
    this.loadNotifications();
    this.loadApplications();
  }

  private loadJobs(): void {
    this.jobsService.getJobs().subscribe({
      next: (jobs) => {
        this.jobs = jobs || [];
      }
    });
  }

  private loadNotifications(): void {
    const userId = this.auth.getUserId();
    if (!userId) {
      return;
    }

    this.notificationsService.getByUser(userId).subscribe({
      next: (items) => {
        this.notifications = items || [];
      }
    });
  }

  private loadApplications(): void {
    const userId = this.auth.getUserId();
    if (!userId) {
      return;
    }

    this.applicationsService.getByUser(userId).subscribe({
      next: (items) => {
        this.applications = items || [];
      }
    });
  }
}

