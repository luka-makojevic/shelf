import {
  ColorProps,
  TypographyProps,
  SpaceProps,
  LayoutProps,
  BorderProps,
  GridProps,
  FlexboxProps,
  VariantArgs,
} from 'styled-system';

export interface ProfileProps {
  hideProfile: boolean;
}
export interface HeaderProps extends ColorProps, SpaceProps {
  children?: React.ReactNode;
}
export interface CardProps
  extends BorderProps,
    SpaceProps,
    TypographyProps,
    ColorProps,
    LayoutProps {
  children: React.ReactNode;
}

export interface FormProps
  extends BorderProps,
    SpaceProps,
    TypographyProps,
    ColorProps,
    LayoutProps {
  children?: React.ReactNode;
}

export interface InputProps
  extends VariantArgs,
    BorderProps,
    SpaceProps,
    LayoutProps {
  children?: React.ReactNode;
}
export interface TextProps
  extends ColorProps,
    TypographyProps,
    SpaceProps,
    BorderProps {
  children?: React.ReactNode;
}
export interface ContainerProps
  extends ColorProps,
    FlexboxProps,
    LayoutProps,
    SpaceProps,
    GridProps {
  children: React.ReactNode;
}
export interface ButtonProps
  extends LayoutProps,
    SpaceProps,
    TypographyProps,
    BorderProps,
    ColorProps {
  children: React.ReactNode;
}
