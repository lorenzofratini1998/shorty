import {Component, inject, PLATFORM_ID, signal} from '@angular/core';
import {isPlatformBrowser} from '@angular/common';

@Component({
  selector: 'app-navbar',
  imports: [],
  templateUrl: './navbar.html'
})
export class Navbar {
  private platformId = inject(PLATFORM_ID);
  isDarkMode = signal(false);

  constructor() {
    if (isPlatformBrowser(this.platformId)) {
      const savedTheme = localStorage.getItem('theme');
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

      if (savedTheme === 'dark' || (!savedTheme && prefersDark)) {
        this.setTheme('dark');
      } else {
        this.setTheme('light');
      }
    }
  }

  toggleTheme() {
    const newTheme = this.isDarkMode() ? 'light' : 'dark';
    this.setTheme(newTheme);
  }

  private setTheme(theme: string) {
    this.isDarkMode.set(theme === 'dark');

    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('theme', theme);
  }

}
