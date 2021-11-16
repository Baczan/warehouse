import {Pipe, PipeTransform} from '@angular/core';

enum Months {
  Sty,
  lut,
  Mar,
  Kwi,
  Maj,
  Cze,
  Lip,
  Sie,
  Wrz,
  Paź,
  Lis,
  Gru
}

@Pipe({
  name: 'customDate'
})
export class CustomDatePipe implements PipeTransform {

  transform(value: number, ...args: unknown[]): string {
    let date = new Date(value);
    return `${Months[date.getMonth()]}  ${date.getDate()}, ${date.getFullYear()}`;
  }

}
