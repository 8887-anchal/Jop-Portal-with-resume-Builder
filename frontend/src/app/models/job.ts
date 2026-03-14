export interface Job {
  id?: number | string;
  title: string;
  description: string;
  location: string;
  salary?: number;
  recruiterId?: number | string;
}
