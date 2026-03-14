import { JobApplication } from '../models/job-application';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../app.constants';

@Injectable({
  providedIn: 'root'
})
export class ApplicationsService {
  private baseUrl = API_BASE_URL;  // ✅ http://localhost:8082/users (no /api)

  constructor(private http: HttpClient) {}

  apply(payload: JobApplication) {
  return this.http.post<JobApplication>(`${API_BASE_URL}/applications/apply`, payload);
}
getByUser(userId: string | number) {
  return this.http.get<JobApplication[]>(`${API_BASE_URL}/applications/user/${userId}`); // ← doesn't exist yet
}
getByRecruiter(recruiterId: string | number) {
  return this.http.get<JobApplication[]>(`${API_BASE_URL}/recruiter/applications`); // ← exists ✅
}
}