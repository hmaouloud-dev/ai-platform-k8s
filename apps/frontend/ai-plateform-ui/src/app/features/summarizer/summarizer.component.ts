import { Component, signal, inject } from '@angular/core';
import { DocumentService } from '../../services/document.service';

@Component({
  selector: 'app-summarizer',
  standalone: true,
  templateUrl: './summarizer.component.html',
  styleUrl: './summarizer.component.scss'
})
export class SummarizerComponent {
  private readonly documentService = inject(DocumentService);

  selectedFile = signal<File | null>(null);
  summary = signal<string | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);
  isDragging = signal(false);

  readonly acceptedFormats = '.pdf,.txt,.docx';

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.setFile(input.files[0]);
    }
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.isDragging.set(true);
  }

  onDragLeave(): void {
    this.isDragging.set(false);
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.isDragging.set(false);
    const file = event.dataTransfer?.files[0];
    if (file) this.setFile(file);
  }

  removeFile(event: MouseEvent): void {
    event.stopPropagation();
    this.selectedFile.set(null);
    this.summary.set(null);
    this.error.set(null);
  }

  summarize(): void {
    const file = this.selectedFile();
    if (!file) return;

    this.loading.set(true);
    this.error.set(null);
    this.summary.set(null);

    this.documentService.summarize(file).subscribe({
      next: (response) => {
        this.summary.set(response.summary);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Une erreur est survenue lors du résumé. Veuillez réessayer.');
        this.loading.set(false);
      }
    });
  }

  formatFileSize(bytes: number): string {
    if (bytes < 1024) return `${bytes} B`;
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
    return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
  }

  private setFile(file: File): void {
    this.selectedFile.set(file);
    this.summary.set(null);
    this.error.set(null);
  }
}