import { Component } from '@angular/core';
import { NgIf } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { JobsService } from '../../services/job.service';
import { Job } from '../../models/job';
import { ApplicationsService } from '../../services/application.service';
import { AuthService } from '../../auth/auth.service';
import { SavedJobsService } from '../../services/saved-job.service';

@Component({
  selector: 'app-job-details',
  imports: [NgIf],
  templateUrl: './job-details.html',
  styleUrl: './job-details.css'
})
export class JobDetailsComponent {
  job: Job | null = null;
  loading = true;
  error = '';
  actionMessage = '';

  constructor(
    private route: ActivatedRoute,
    private jobsService: JobsService,
    private applicationsService: ApplicationsService,
    private auth: AuthService,
    private savedJobs: SavedJobsService
  ) {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadJob(id);
    } else {
      this.loading = false;
      this.error = 'Job not found.';
    }
  }

  apply(): void {
    if (!this.job?.id) {
      return;
    }

    const userId = this.auth.getUserId();
    if (!userId) {
      this.actionMessage = 'Please login again to apply.';
      return;
    }

    this.actionMessage = '';
    this.applicationsService
      .apply({ jobId: this.job.id, jobSeekerId: userId, status: 'APPLIED' })
      .subscribe({
        next: () => {
          this.actionMessage = 'Application submitted successfully.';
        },
        error: (err) => {
          this.actionMessage = err?.error?.message || 'Unable to apply right now.';
        }
      });
  }

  saveJob(): void {
    if (!this.job?.id) {
      return;
    }

    const userId = this.auth.getUserId();
    if (!userId) {
      this.actionMessage = 'Please login again to save.';
      return;
    }

    this.actionMessage = '';
    this.savedJobs.saveJob(userId, this.job.id).subscribe({
      next: () => {
        this.actionMessage = 'Job saved successfully.';
      },
      error: (err) => {
        this.actionMessage = err?.error?.message || 'Unable to save job.';
      }
    });
  }

  private loadJob(id: string): void {
    this.jobsService.getJobById(id).subscribe({
      next: (job) => {
        this.job = job;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message || 'Unable to load job.';
      }
    });
  }
}

