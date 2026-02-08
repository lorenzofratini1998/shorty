import {Injectable, signal} from '@angular/core';

export interface Toast {
  id: number;
  message: string;
  type: 'success' | 'error' | 'info' | 'warning';
}

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  private _toasts = signal<Toast[]>([]);
  readonly toasts = this._toasts.asReadonly();

  showToast(message: string, type: 'success' | 'error' | 'info' | 'warning' = 'info', duration = 4000) {
    const id = Date.now();
    const toast: Toast = { id, message, type };

    this._toasts.update(current => [...current, toast]);

    setTimeout(() => this.removeToast(id), duration);
  }

  removeToast(id: number) {
    this._toasts.update(current => current.filter(t => t.id !== id));
  }
}
