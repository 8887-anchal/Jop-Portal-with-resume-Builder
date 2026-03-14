import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { API_ROOT_URL } from '../app.constants';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  constructor(private http: HttpClient) {}

  getUsers() {
    return this.http.get<User[]>(`${API_ROOT_URL}/users`);
  }
}

