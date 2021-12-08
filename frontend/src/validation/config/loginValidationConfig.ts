import { LoginFieldConfig } from '../../interfaces/types';
import { emailRegex } from '../regex';

export const loginFieldConfig: LoginFieldConfig[] = [
  {
    type: 'text',
    placeholder: 'Email',
    name: 'email',
    validations: {
      required: 'This field is required',
      pattern: {
        value: emailRegex,
        message: 'Invalid email format',
      },
    },
  },
  {
    type: 'password',
    placeholder: 'Password',
    name: 'password',
    validations: {
      required: 'This field is required',
      minLength: {
        value: 8,
        message: 'Password must have at least 8 characters',
      },
    },
  },
];
