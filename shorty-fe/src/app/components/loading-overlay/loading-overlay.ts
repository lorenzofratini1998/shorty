import {Component, inject} from '@angular/core';
import {LoadingService} from '../../services/loading.service';

@Component({
  selector: 'app-loading-overlay',
  imports: [],
  template: `
    @if (loadingService.isLoading()) {
      <div class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm cursor-wait">
        <div class="flex flex-col items-center gap-4">
          <span class="loading loading-spinner loading-xl text-primary"></span>
        </div>
      </div>
    }
  `
})
export class LoadingOverlay {
  loadingService = inject(LoadingService);
}
