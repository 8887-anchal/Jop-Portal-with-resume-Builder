import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';

import { JobsService } from '../../services/job.service';
import { AuthService } from '../../auth/auth.service';
import { Job } from '../../models/job';
import { ApplicationsService, JobApplication } from '../../services/application.service';

@Component({
  selector: 'app-recruiter-dashboard',
  imports: [FormsModule, NgIf, NgFor],
  templateUrl: './recruiter-dashboard.html',
  styleUrl: './recruiter-dashboard.css'
})
export class RecruiterDashboardComponent {
  title = '';
  description = '';
  location = '';
  salary: number | null = null;
  message = '';
  loading = false;
  jobs: Job[] = [];
  applicants: JobApplication[] = [];

  constructor(
    private jobsService: JobsService,
    private auth: AuthService,
    private applicationsService: ApplicationsService
  ) {
    this.loadRecruiterJobs();
    this.loadApplicants();
  }

  postJob(): void {
    this.message = '';
    this.loading = true;

    const recruiterId = this.auth.getUserId();

    this.jobsService
      .postJob({
        title: this.title,
        description: this.description,
        location: this.location,
        salary: this.salary || undefined,
        recruiterId: recruiterId ? Number(recruiterId) : undefined
      })
      .subscribe({
        next: () => {
          this.loading = false;
          this.message = 'Job posted successfully.';
          this.title = '';
          this.description = '';
          this.location = '';
          this.salary = null;
          this.loadRecruiterJobs();
        },
        error: (err) => {
          this.loading = false;
          this.message = err?.error?.message || 'Unable to post job.';
        }
      });
  }

  private loadRecruiterJobs(): void {
    const recruiterId = this.auth.getUserId();
    if (!recruiterId) {
      return;
    }

    this.jobsService.getJobsByRecruiter(recruiterId).subscribe({
      next: (jobs) => {
        this.jobs = jobs || [];
      },
      error: () => {
        this.jobs = [];
      }
    });
  }

  private loadApplicants(): void {
    const recruiterId = this.auth.getUserId();
    if (!recruiterId) {
      return;
    }

    this.applicationsService.getByRecruiter(recruiterId).subscribe({
      next: (items) => {
        this.applicants = items || [];
      },
      error: () => {
        this.applicants = [];
      }
    });
  }
}

