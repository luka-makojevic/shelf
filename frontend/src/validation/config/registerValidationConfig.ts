import React from 'react';
import { useForm } from 'react-hook-form';
import { RegisterFormData } from '../../interfaces/types';
import { RegisterFieldConfig } from '../../interfaces/types';

export const config = (watch: any): RegisterFieldConfig[] => [
  {
    type: 'email',
    placeholder: 'Email',
    name: 'email',
    validations: {
      required: 'This field is required',
      pattern: {
        value:
          /^(([^<>()[\].,;:\s@"]+(\.[^<>()[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/i,
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
      pattern: {
        value:
          /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&.()–[{}\]:;',?/*~$^+=<>])([^\s]){8,}$/i,
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
    },
  },
  {
    type: 'text',
    placeholder: 'Last Name',
    name: 'lastName',
    validations: {
      required: 'This field is required',
    },
  },
];
