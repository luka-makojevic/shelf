import { UseFormWatch } from 'react-hook-form';
import {
  ManageProfileFieldConfig,
  ManageProfileFormData,
} from '../../../interfaces/dataTypes';
import { passwordRegex } from '../regex';

export const config = (
  watch: UseFormWatch<ManageProfileFormData>
): ManageProfileFieldConfig[] => [
  {
    type: 'text',
    placeholder: 'Email',
    name: 'email',
    validations: {},
  },
  {
    type: 'text',
    placeholder: 'First Name',
    name: 'firstName',
    validations: {
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
      maxLength: {
        value: 50,
        message: 'Last name can not be longer than 50 characters',
      },
    },
  },

  {
    type: 'password',
    placeholder: 'Password',
    name: 'password',
    validations: {
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
      validate: (value: string) =>
        value === watch('password') || 'Passwords must match',
    },
  },
];
