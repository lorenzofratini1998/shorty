import {Component, inject, signal} from '@angular/core';
import {LambdaService} from '../../services/lambda.service';
import {ShortenRequest} from '../../models/url.model';
import {FormsModule} from '@angular/forms';
import {LoadingService} from '../../services/loading.service';
import {ToastService} from '../../services/toast.service';
import {PrettyUrlPipe} from '../../pipes/pretty-url-pipe';
import {HistoryItem, HistoryService} from '../../services/history.service';

@Component({
  selector: 'app-shortener',
  imports: [
    FormsModule,
    PrettyUrlPipe
  ],
  templateUrl: './shortener.html'
})
export class Shortener {
  private readonly urlService = inject(LambdaService);
  private readonly loadingService = inject(LoadingService);
  private readonly toastService = inject(ToastService);
  private readonly historyService = inject(HistoryService);

  originalUrl = signal('');
  customAlias = signal('');
  result = signal<string | null>(null);
  copied = signal(false);

  submit() {
    this.loadingService.showLoader();
    this.result.set(null);

    const payload: ShortenRequest = {
      originalUrl: this.originalUrl(),
      ttlInDays: 30
    };

    const alias = this.customAlias().trim();

    if (alias && alias.length >= 5) {
      payload.customAlias = alias;
    } else if (alias.length > 0 && alias.length < 5) {
      this.loadingService.hideLoader();
      this.toastService.showToast('Alias must be at least 5 characters', 'error');
      return;
    }

    this.urlService.shorten(payload).subscribe({
      next: (response) => {
        this.loadingService.hideLoader();
        this.result.set(response.shortUrl);

        const historyItem: HistoryItem = {
          originalUrl: response.originalUrl,
          shortUrl: response.shortUrl,
          alias: response.alias,
          createdAt: new Date()
        };

        this.historyService.addToHistory(historyItem);
        this.toastService.showToast('Short link created successfully!', 'success');
      },
      error: (err) => {
        console.error('Error occurred:', err);
        this.loadingService.hideLoader();
        this.toastService.showToast(err.message || 'An error occurred', 'error');
      }
    });
  }

  copyToClipboard() {
    const url = this.result();
    if (!url) return;

    navigator.clipboard.writeText(url);
    this.copied.set(true);
    this.toastService.showToast('Copied to clipboard!', 'info', 2000);
    setTimeout(() => this.copied.set(false), 2000);
  }

  reset() {
    this.originalUrl.set('');
    this.customAlias.set('');
    this.result.set(null);
  }
}
