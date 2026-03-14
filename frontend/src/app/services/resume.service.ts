import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { API_BASE_URL } from '../app.constants';

export interface Resume {
  id?: number;
  userId: number | string;
  summary: string;
  [key: string]: any;
}

@Injectable({
  providedIn: 'root'
})
export class ResumeService {
  constructor(private http: HttpClient) {}

  createResume(payload: Resume) {
    return this.http.post<Resume>(`${API_BASE_URL}/resume/create`, payload);
  }

  getResume(id: number | string) {
    return this.http.get<Resume>(`${API_BASE_URL}/resume/${id}`);
  }
}
