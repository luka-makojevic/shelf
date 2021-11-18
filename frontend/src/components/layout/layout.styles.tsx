import styled from 'styled-components'
import { layout, flexbox, space, color } from 'styled-system'
import { ContainerProps } from '../../interfaces/styles'

const Wrapper = styled.div<ContainerProps>`
  display: flex;
  align-items: center;
  justify-content: center;

  width: 100vw;
  height: 100vh;

  ${space}
  ${layout}
  ${flexbox}
`
const Container = styled.div<ContainerProps>`
  display: flex;
  align-items: center;
  justify-content: center;

  height: 100%;
  ${space}
  ${layout}
${flexbox}
${color}
`

const Feature = styled.div<ContainerProps>`
  display: flex;
  flex-direction: column;
  align-items: center;

  margin: 0 1em;
  padding: 1em;
  height: 550px;
  max-width: 400px;
  max-height: 550px;

  text-align: center;
  color: white;
  border-color: white;
  border: 1px solid white;
  border-radius: 50px;

  ${space}
  ${layout}
  ${flexbox}
  ${color}
`
export { Wrapper, Container, Feature }
