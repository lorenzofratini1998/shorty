import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'prettyUrl',
})
export class PrettyUrlPipe implements PipeTransform {

  transform(value: string | null): string {
    if (!value) return '';

    const code = value.split('/r/').pop();

    return `https://shrt.y/r/${code}`;
  }

}
