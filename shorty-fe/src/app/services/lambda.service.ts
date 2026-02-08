import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {ShortenRequest, ShortenResponse} from '../models/url.model';
import {catchError, Observable, throwError} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LambdaService {
  private readonly http = inject(HttpClient);

  private baseUrl = environment.apiUrl.replace(/\/$/, '');

  shorten(payload: ShortenRequest): Observable<ShortenResponse> {
    return this.http.post<ShortenResponse>(`${this.baseUrl}/api/v1/url/shorten`, payload)
      .pipe(
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unexpected error occurred.';
    if (error.error instanceof ErrorEvent) {
      errorMessage = error.error.message;
    } else {
      errorMessage = error.error?.detail || error.error?.message || `Error ${error.status}`;
    }
    return throwError(() => new Error(errorMessage));
  }
}
