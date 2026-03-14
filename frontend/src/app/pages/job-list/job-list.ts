import { Component } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';

import { JobsService } from '../../services/job.service';
import { Job } from '../../models/job';

@Component({
  selector: 'app-job-list',
  imports: [NgFor, RouterLink, NgIf],
  templateUrl: './job-list.html',
  styleUrl: './job-list.css'
})
export class JobListComponent {
  jobs: Job[] = [];
  loading = true;
  error = '';

  constructor(private jobsService: JobsService) {
    this.loadJobs();
  }

  private loadJobs(): void {
    this.jobsService.getJobs().subscribe({
      next: (jobs) => {
        this.jobs = jobs || [];
        this.loading = false;
      },
      error: (err) => {
        this.error = err?.error?.message || 'Unable to load jobs.';
        this.loading = false;
      }
    });
  }
}

