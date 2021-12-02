import { FieldValues, UseFormWatch } from 'react-hook-form';
import { ResetPasswordFieldConfig } from '../../interfaces/types';
import { passwordRegex } from '../regex';

export const config = (
  watch: UseFormWatch<FieldValues>
): ResetPasswordFieldConfig[] => [
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
];
