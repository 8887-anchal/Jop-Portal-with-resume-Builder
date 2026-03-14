import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { API_BASE_URL } from '../app.constants';

export interface JobApplication {
  id?: number;
  jobId: number | string;
  jobSeekerId: number | string;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class ApplicationsService {
  constructor(private http: HttpClient) {}

  apply(payload: JobApplication) {
    return this.http.post<JobApplication>(`${API_BASE_URL}/applications/apply`, payload);
  }

  getByUser(userId: string | number) {
    return this.http.get<JobApplication[]>(`${API_BASE_URL}/applications/user/${userId}`);
  }

  getByRecruiter(recruiterId: string | number) {
    return this.http.get<JobApplication[]>(`${API_BASE_URL}/applications/recruiter/${recruiterId}`);
  }
}
