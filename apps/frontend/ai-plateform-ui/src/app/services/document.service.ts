import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SummaryResponse {
  summary: string;
}

@Injectable({ providedIn: 'root' })
export class DocumentService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/documents/summarize';

  summarize(file: File): Observable<SummaryResponse> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<SummaryResponse>(this.apiUrl, formData);
  }
}