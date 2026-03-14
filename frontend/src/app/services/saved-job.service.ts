import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { API_BASE_URL } from '../app.constants';

@Injectable({
  providedIn: 'root'
})
export class SavedJobsService {
  constructor(private http: HttpClient) {}

  saveJob(userId: number | string, jobId: number | string) {
    return this.http.post(`${API_BASE_URL}/saved-jobs`, { userId, jobId });
  }
}
