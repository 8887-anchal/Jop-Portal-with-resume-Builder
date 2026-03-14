import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  register(user: any): Observable<any> {
    return this.http.post(this.baseUrl + '/users/register', user);
  }

  getJobs(): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      Authorization: 'Bearer ' + token
    });
    return this.http.get(this.baseUrl + '/api/jobs/all', { headers });
  }

  getJob(id: number): Observable<any> {
    return this.http.get(this.baseUrl + '/api/jobs/' + id);
  }

  postJob(job: any): Observable<any> {
    return this.http.post(this.baseUrl + '/api/jobs/post', job);
  }

  applyJob(application: any): Observable<any> {
    return this.http.post(this.baseUrl + '/api/applications/apply', application);
  }

  createResume(resume: any): Observable<any> {
    return this.http.post(this.baseUrl + '/api/resume/create', resume);
  }
}

