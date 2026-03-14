import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { API_BASE_URL } from '../app.constants';
import { Job } from '../models/job';

@Injectable({
  providedIn: 'root'
})
export class JobsService {
  constructor(private http: HttpClient) {}

 getJobs() {
  return this.http.get<Job[]>(`${API_BASE_URL}/jobs/all`);
}
getJobById(id: string | number) {
  return this.http.get<Job>(`${API_BASE_URL}/jobs/${id}`);
}
getJobsByRecruiter(recruiterId: string | number) {
  return this.http.get<Job[]>(`${API_BASE_URL}/recruiter/jobs`); // ← recruiter endpoint
}
postJob(payload: Partial<Job>) {
  return this.http.post<Job>(`${API_BASE_URL}/jobs/post`, payload); // ← becomes /api/jobs/post ✅
}
}
