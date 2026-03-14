export interface JobApplication {
  id?: number;
  jobId: number | string;
  jobSeekerId: number | string;
  status: string;
}