import {Component, Input, OnInit} from '@angular/core';
import {FormControl} from '@angular/forms';

@Component({
  selector: 'app-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.css']
})
export class InputComponent implements OnInit {

  @Input() label: string;

  @Input() control: FormControl;

  @Input() type: string;

  @Input() name: string = '';

  @Input() autocomplete = '';

  @Input() loginError = false;

  constructor() {
  }

  ngOnInit(): void {

  }


  getErrorMessage() {

    if (this.loginError) {
      return 'Niepoprawny email lub hasło';
    }

    if (this.control.hasError('required')) {
      return 'wymagane';
    }

    if (this.control.hasError('pattern')) {
      return 'hasło musi mieć przynajmniej 8 znaków, zawierać małe i duże litery oraz liczbę';
    }

    if (this.control.hasError('dontMatch')) {
      return 'hasła się nie zgadzają';
    }

    if (this.control.hasError('already_exists')) {
      return 'użytownik o podanym adresie e-mail już istnieje';
    }
    if (this.control.hasError('user_not_found')) {
      return 'nie ma użytkownika o podanym adresie email';
    }


    return '';
  }
}
