import {Component, inject} from '@angular/core';
import {HistoryService} from '../../services/history.service';
import {PrettyUrlPipe} from '../../pipes/pretty-url-pipe';
import {DatePipe} from '@angular/common';
import {ToastService} from '../../services/toast.service';

@Component({
  selector: 'app-history',
  imports: [
    PrettyUrlPipe,
    DatePipe
  ],
  templateUrl: './history.html'
})
export class History {
  protected readonly historyService = inject(HistoryService);
  private readonly toastService = inject(ToastService);

  copyToClipboard(url: string) {
    if (!url) return;
    navigator.clipboard.writeText(url);
    this.toastService.showToast('Link copied to clipboard!', 'info');
  }
}
