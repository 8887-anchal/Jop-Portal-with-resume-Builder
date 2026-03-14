import { Component } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';

import { ApplicationsService, JobApplication } from '../../services/application.service';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-applications',
  imports: [NgFor, NgIf],
  templateUrl: './applications.html',
  styleUrls: ['./applications.css']
})
export class ApplicationsComponent {
  applications: JobApplication[] = [];
  loading = true;
  error = '';

  constructor(private applicationsService: ApplicationsService, private auth: AuthService) {
    this.loadApplications();
  }

  private loadApplications(): void {
    const userId = this.auth.getUserId();
    if (!userId) {
      this.error = 'Please login again to view applications.';
      this.loading = false;
      return;
    }

    this.applicationsService.getByUser(userId).subscribe({
      next: (items) => {
        this.applications = items || [];
        this.loading = false;
      },
      error: (err) => {
        this.error = err?.error?.message || 'Unable to load applications.';
        this.loading = false;
      }
    });
  }
}

