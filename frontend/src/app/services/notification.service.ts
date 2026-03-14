import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { API_BASE_URL } from '../app.constants';

export interface NotificationItem {
  message: string;
  date: string;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {
  constructor(private http: HttpClient) {}

  getByUser(userId: number | string) {
    return this.http.get<NotificationItem[]>(`${API_BASE_URL}/notifications/${userId}`);
  }
}
