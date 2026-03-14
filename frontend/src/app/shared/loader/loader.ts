import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-shared-loader',
  standalone: true,
  templateUrl: './loader.html',
  styleUrl: './loader.css'
})
export class SharedLoaderComponent {
  @Input() label = 'Loading...';
}
