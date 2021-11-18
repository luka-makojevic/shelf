import {
  ColorProps,
  TypographyProps,
  SpaceProps,
  LayoutProps,
  BorderProps,
  FlexboxProps,
} from 'styled-system'

export interface ProfileProps {
  hideProfile: boolean
}

export interface FormProps
  extends BorderProps,
    SpaceProps,
    TypographyProps,
    ColorProps,
    LayoutProps {
  children?: React.ReactNode
}
export interface TextProps extends ColorProps, TypographyProps, SpaceProps {
  children?: React.ReactNode
}
export interface ContainerProps
  extends ColorProps,
    FlexboxProps,
    LayoutProps,
    SpaceProps {
  children: React.ReactNode
}
