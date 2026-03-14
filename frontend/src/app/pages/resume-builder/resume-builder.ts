import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../auth/auth.service';
import { ResumeService } from '../../services/resume.service';

@Component({
  selector: 'app-resume-builder',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './resume-builder.html',
  styleUrl: './resume-builder.css'
})
export class ResumeBuilderComponent {
  loading = false;
  saving = false;
  message = '';
  resume = {
    name: '', title: '', location: '', phone: '', email: '', linkedin: '', github: '', summary: '',
    skillCategories: [
      { label: 'Languages',   value: '', placeholder: 'Java, Python, JavaScript, SQL' },
      { label: 'Frameworks',  value: '', placeholder: 'Spring Boot, Angular, Hibernate' },
      { label: 'Databases',   value: '', placeholder: 'MySQL, PostgreSQL, MongoDB' },
      { label: 'Tools',       value: '', placeholder: 'Git, Maven, IntelliJ IDEA, Docker' },
      { label: 'Cloud & AI',  value: '', placeholder: 'AWS, Azure, GCP' },
      { label: 'Core Skills', value: '', placeholder: 'OOP, DSA, Agile, REST APIs' },
    ],
    experience:     [{ title:'', company:'', location:'', duration:'', bullets:'' }],
    projects:       [] as any[],
    education:      [{ degree:'', institution:'', year:'', score:'' }],
    certifications: [] as any[],
  };

  constructor(
    private http: HttpClient,
    private auth: AuthService,
    private resumeService: ResumeService
  ) {}

  hasSkills(): boolean {
    return this.resume.skillCategories.some(c => c.value.trim() !== '');
  }

  addExperience()             { this.resume.experience.push({ title:'', company:'', location:'', duration:'', bullets:'' }); }
  removeExperience(i: number) { this.resume.experience.splice(i, 1); }
  addProject()                { this.resume.projects.push({ name:'', tech:'', bullets:'' }); }
  removeProject(i: number)    { this.resume.projects.splice(i, 1); }
  addEducation()              { this.resume.education.push({ degree:'', institution:'', year:'', score:'' }); }
  removeEducation(i: number)  { this.resume.education.splice(i, 1); }
  addCert()                   { this.resume.certifications.push({ title:'', issuer:'', date:'' }); }
  removeCert(i: number)       { this.resume.certifications.splice(i, 1); }

  generateResume(): void {
    if (!this.resume.name.trim()) { alert('Please enter your full name.'); return; }
    this.loading = true;
    this.message = '';
    this.http.post('http://localhost:8082/resume/generate', this.resume, { responseType: 'blob' })
      .subscribe({
        next: (blob: Blob) => {
          this.loading = false;
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `${this.resume.name.replace(/\s+/g, '_')}_Resume.pdf`;
          a.click();
          window.URL.revokeObjectURL(url);
        },
        error: (err) => {
          this.loading = false;
          console.error(err);
          alert('Failed to generate PDF. Make sure the backend is running.');
        }
      });
  }

  saveResume(): void {
    if (!this.resume.name.trim()) { alert('Please enter your full name.'); return; }
    const userId = this.auth.getUserId();
    if (!userId) { alert('Please log in to save your resume.'); return; }

    this.saving = true;
    this.message = '';
    const payload = { ...this.resume, userId };
    this.resumeService.createResume(payload).subscribe({
      next: () => {
        this.saving = false;
        this.message = 'Resume saved successfully.';
      },
      error: (err) => {
        this.saving = false;
        console.error(err);
        this.message = 'Failed to save resume. Make sure the backend is running.';
      }
    });
  }
}
