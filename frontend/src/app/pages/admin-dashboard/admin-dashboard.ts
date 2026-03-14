import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-dashboard.html'
})
export class AdminDashboardComponent implements OnInit {
  users: any[] = [];
  jobs: any[] = [];
  loading = false;
  error = '';

  constructor(private http: HttpClient, private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.loadUsers();
    this.loadJobs();
  }

  loadUsers() {
    this.loading = true;
    this.http.get('http://localhost:8082/users').subscribe({
      next: (data: any) => {
        this.users = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.error = 'Failed to load users';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadJobs() {
    this.http.get('http://localhost:8082/api/jobs/all').subscribe({
      next: (data: any) => {
        this.jobs = data;
        this.cdr.detectChanges();
      },
      error: () => {}
    });
  }
}