export interface ProfileProps {
  hideProfile?: boolean;
}
export interface HeaderProps {
  children?: React.ReactNode;
}
export interface CardProps {
  children: React.ReactNode;
}

export interface FormProps {
  children?: React.ReactNode;
}

export interface InputProps {
  children?: React.ReactNode;
}
export interface TextProps {
  children?: React.ReactNode;
}
export interface ContainerProps {
  background?: string;
  children: React.ReactNode;
  isHiddenOnSmallScreen?: boolean
}
export interface ButtonStyleProps {
  children: React.ReactNode;
  variant?: string;
  size?: string;
  fullwidth?: boolean;
}
