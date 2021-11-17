import styled from 'styled-components'
import {
  layout,
  flexbox,
  space,
  color,
} from 'styled-system'
import { ContainerProps } from '../../interfaces/styles'


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

  
  
`

const Feature = styled.div <ContainerProps>`
  ${space}
  ${layout}
  ${flexbox}
  ${color} 
  color: white;
  border-color: white;
  padding: 7em 3em;
  max-width: 600px;
  border: 1px solid white;
  border-radius: 50px;
  text-align: center;
`
export { Inner, Container, Feature }
