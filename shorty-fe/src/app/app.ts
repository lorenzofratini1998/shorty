import {Component, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Shortener} from './components/shortener/shortener';
import {Navbar} from './components/navbar/navbar';
import {LoadingOverlay} from './components/loading-overlay/loading-overlay';
import {ToastContainer} from './components/toast-container/toast-container';
import {History} from './components/history/history';

@Component({
  selector: 'app-root',
  imports: [FormsModule, Shortener, Navbar, LoadingOverlay, ToastContainer, History],
  template: `
    <app-loading-overlay></app-loading-overlay>
    <app-toast-container></app-toast-container>

    <div class="min-h-screen flex flex-col bg-base-200">

      <app-navbar></app-navbar>

      <main class="flex-grow flex flex-col items-center justify-center p-4 gap-8">
        <app-shortener class="w-full flex justify-center"></app-shortener>
        <app-history class="w-full flex justify-center"></app-history>
      </main>

      <footer class="footer footer-center p-4 text-base-content/50">
        <aside>
          <p>Copyright © {{ year() }} - All rights reserved</p>
        </aside>
      </footer>

    </div>
  `
})
export class App {
  year = signal(new Date().getFullYear());
}
