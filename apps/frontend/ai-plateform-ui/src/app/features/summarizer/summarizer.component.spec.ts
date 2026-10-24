import { TestBed, ComponentFixture } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { SummarizerComponent } from './summarizer.component';
import { DocumentService } from '../../services/document.service';

describe('SummarizerComponent', () => {
  let fixture: ComponentFixture<SummarizerComponent>;
  let component: SummarizerComponent;
  const documentService = { summarize: vi.fn() };

  beforeEach(async () => {
    documentService.summarize.mockReset();
    await TestBed.configureTestingModule({
      imports: [SummarizerComponent],
      providers: [{ provide: DocumentService, useValue: documentService }],
    }).compileComponents();

    fixture = TestBed.createComponent(SummarizerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  const selectFile = (name = 'rapport.pdf') => {
    const file = new File(['contenu'], name, { type: 'application/pdf' });
    const input = { files: [file] } as unknown as HTMLInputElement;
    component.onFileSelected({ target: input } as unknown as Event);
    fixture.detectChanges();
    return file;
  };

  it('disables the button until a file is selected', () => {
    const button = fixture.nativeElement.querySelector('.btn-summarize') as HTMLButtonElement;
    expect(button.disabled).toBe(true);

    selectFile();

    expect(button.disabled).toBe(false);
    expect(fixture.nativeElement.querySelector('.file-name').textContent).toContain('rapport.pdf');
  });

  it('displays the summary returned by the API', () => {
    documentService.summarize.mockReturnValue(of({ summary: 'Résumé de test' }));
    const file = selectFile();

    component.summarize();
    fixture.detectChanges();

    expect(documentService.summarize).toHaveBeenCalledWith(file);
    expect(fixture.nativeElement.querySelector('.result p').textContent).toContain('Résumé de test');
    expect(component.loading()).toBe(false);
  });

  it('displays an error when the API fails', () => {
    documentService.summarize.mockReturnValue(throwError(() => new Error('boom')));
    selectFile();

    component.summarize();
    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('.error-box')).toBeTruthy();
    expect(component.summary()).toBeNull();
  });

  it('formats file sizes', () => {
    expect(component.formatFileSize(500)).toBe('500 B');
    expect(component.formatFileSize(2048)).toBe('2.0 KB');
    expect(component.formatFileSize(3 * 1024 * 1024)).toBe('3.0 MB');
  });
});
