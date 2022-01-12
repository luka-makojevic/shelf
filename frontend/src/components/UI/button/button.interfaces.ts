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
  variant?: string;
  size?: string;
  fullwidth?: boolean;
  disabled?: boolean;
  type?: 'button' | 'reset' | 'submit' | undefined;
}
