import {effect, Injectable, signal} from '@angular/core';

export interface HistoryItem {
  originalUrl: string;
  shortUrl: string;
  alias: string;
  createdAt: Date;
}

@Injectable({
  providedIn: 'root',
})
export class HistoryService {
  private _history = signal<HistoryItem[]>([]);
  readonly history = this._history.asReadonly();

  constructor() {
    const saved = localStorage.getItem('shorty_history');
    if (saved) {
      try {
        this._history.set(JSON.parse(saved));
      } catch (e) {
        console.error('Error parsing history', e);
      }
    }

    effect(() => {
      localStorage.setItem('shorty_history', JSON.stringify(this._history()));
    });
  }

  addToHistory(item: HistoryItem) {
    this._history.update(current => {
      return [item, ...current].slice(0, 5);
    });
  }

  clearHistory() {
    this._history.set([]);
  }
}
