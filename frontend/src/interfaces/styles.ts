import {
  ColorProps,
  TypographyProps,
  SpaceProps,
  LayoutProps,
  BorderProps,
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
  children: React.ReactNode
}
