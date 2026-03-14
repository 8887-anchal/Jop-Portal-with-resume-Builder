export const STORAGE_KEYS = {
  token: 'auth_token',
  userId: 'auth_user_id',
  userRole: 'auth_user_role',
  userEmail: 'auth_user_email'   // ✅ added back
};

// ✅ Used by application.service, job.service, notification.service, resume.service, saved-job.service
export const API_BASE_URL = 'http://localhost:8082/api';
export const API_ROOT_URL = 'http://localhost:8082';