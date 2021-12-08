import { ChangeEventHandler } from 'react';
import { FieldError } from 'react-hook-form';
import { InputFieldType } from './dataTypes';

export type CardProps = { image: string; text: string; alt: string };

export type CheckBoxProps = {
  onChange?: ChangeEventHandler;
  name?: string;
  id: string;
  children: React.ReactNode;
};

export type HeaderProps = {
  hideProfile?: boolean;
  position?: string;
  showButtons?: boolean;
};

export interface TextCardProps {
  title?: string;
  subtitle?: string;
  text: string;
}
export type InputFieldProps = {
  name?: string;
  placeholder?: string;
  onChange?: ChangeEventHandler;
  error?: FieldError;
  type?: InputFieldType;
  value?: string;
};

export interface FormProps {
  children: React.ReactNode;
}

export interface FormBaseProps {
  children: React.ReactNode;
  onSubmit: React.FormEventHandler<HTMLFormElement>;
}

export interface FormSubmitProps {
  children: React.ReactNode;
  isLoading: boolean;
}

export interface ButtonProps {
  children?: React.ReactNode;
  icon?: JSX.Element;
  to?: string;
  isLoading?: boolean;
  spinner?: boolean;
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
  variant?: string;
  size?: string;
  fullwidth?: boolean;
}
