import { UseFormWatch } from 'react-hook-form';
import {
  RegisterFieldConfig,
  RegisterFormData,
} from '../../interfaces/dataTypes';
import { emailRegex, passwordRegex } from '../regex';

export const config = (
  watch: UseFormWatch<RegisterFormData>
): RegisterFieldConfig[] => [
  {
    type: 'text',
    placeholder: 'Email',
    name: 'email',
    validations: {
      required: 'This field is required',
      maxLength: {
        value: 50,
        message: 'Email can not be longer than 50 characters',
      },
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
      maxLength: {
        value: 50,
        message: 'Password can not be longer than 50 characters',
      },
      pattern: {
        value: passwordRegex,
        message: 'Invalid password format',
      },
    },
  },
  {
    type: 'password',
    placeholder: 'Confirm Password',
    name: 'confirmPassword',
    validations: {
      required: 'This field is required',
      validate: (value: string) =>
        value === watch('password') || 'Passwords must match',
    },
  },
  {
    type: 'text',
    placeholder: 'First Name',
    name: 'firstName',
    validations: {
      required: 'This field is required',
      maxLength: {
        value: 50,
        message: 'First name can not be longer than 50 characters',
      },
    },
  },
  {
    type: 'text',
    placeholder: 'Last Name',
    name: 'lastName',
    validations: {
      required: 'This field is required',
      maxLength: {
        value: 50,
        message: 'Last name can not be longer than 50 characters',
      },
    },
  },
];
