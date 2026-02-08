import {Injectable, signal} from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class LoadingService {
  private _isLoading = signal(false);
  readonly isLoading = this._isLoading.asReadonly();

  showLoader() {
    this._isLoading.set(true);
  }

  hideLoader() {
    this._isLoading.set(false);
  }
}
