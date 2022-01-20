export interface ButtonStyleProps {
  children: React.ReactNode;
  variant?: string;
  size?: string;
  fullwidth?: boolean;
}

export interface ButtonProps {
  children?: React.ReactNode;
  icon?: JSX.Element;
  to?: string;
  isLoading?: boolean;
  spinner?: boolean;
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
  variant?:
    | 'primary'
    | 'secondary'
    | 'light'
    | 'lightBordered'
    | 'primaryBordered'
    | undefined;
  size?: string;
  fullwidth?: boolean;
  disabled?: boolean;
  visible?: boolean;
  type?: 'button' | 'reset' | 'submit' | undefined;
  form?: string;
}
