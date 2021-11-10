import { ReactNode } from 'react'
import styled from 'styled-components'
import { space, SpaceProps, typography, TypographyProps } from 'styled-system'

interface TitleProps extends TypographyProps, SpaceProps {
  children: ReactNode
}

export const Container = styled.div``
export const Title = styled.h1<TitleProps>`
  ${typography}
  ${space}
`
export const Logo = styled.img``
export const SubTitle = styled.p``
