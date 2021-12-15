import { LoginFieldConfig } from '../../../interfaces/dataTypes';

export const loginFieldConfig: LoginFieldConfig[] = [
  {
    type: 'text',
    placeholder: 'Email',
    name: 'email',
    validations: {
      required: 'This field is required',
    },
  },
  {
    type: 'password',
    placeholder: 'Password',
    name: 'password',
    validations: {
      required: 'This field is required',
    },
  },
];
