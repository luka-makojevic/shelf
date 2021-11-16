import styled from 'styled-components'
import {
  layout,
  LayoutProps,
  flexbox,
  FlexboxProps,
  space,
  SpaceProps,
  typography,
  TypographyProps,
  color,
  ColorProps,
} from 'styled-system'

interface ContainerProps
  extends ColorProps,
    FlexboxProps,
    LayoutProps,
    SpaceProps {
  children: React.ReactNode
}

const Inner = styled.div<ContainerProps>`
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;

  ${space}
  ${layout}
  ${flexbox}
`
const Container = styled.div<ContainerProps>`
  ${space}
  ${layout}
  ${flexbox}
  ${color}
  display: flex;
  align-items: center;
  justify-content: center;

  width: 50%;
  height: 100%;
`

const Feature = styled.div`
  color: white;
  border-color: white;
  padding: 7em 3em;
  max-width: 600px;
  border: 1px solid white;
  border-radius: 50px;
  text-align: center;
`

const Title = styled.h1`
  font-size: 5rem;
  @media (max-width: 1200px) {
    font-size: 3rem;
  }
`
const SubTitle = styled.p``
export { SubTitle, Title, Inner, Container, Feature }
