import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Skill } from '../models/skill';
import { Project } from '../models/project';

@Injectable({
  providedIn: 'root'
})
export class PortfolioService {
  private readonly api = 'http://localhost:8082/api';

  constructor(private http: HttpClient) {}

  getProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.api}/projects`);
  }

  getSkills(): Observable<Skill[]> {
    return this.http.get<Skill[]>(`${this.api}/skills`);
  }

  addSkill(payload: Skill): Observable<Skill> {
    return this.http.post<Skill>(`${this.api}/skills`, payload);
  }

  updateSkill(id: number, payload: Skill): Observable<Skill> {
    return this.http.put<Skill>(`${this.api}/skills/${id}`, payload);
  }

  deleteSkill(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/skills/${id}`);
  }
}
