import styled from 'styled-components'
import { Container } from '../form/form-styles'

export const Inner = styled.div`
  height: 100vh;
  width: 100vw;
  display: flex;
`
export const ContainerRight = styled.div`
  width: 50%;
  height: 100%;
  padding: 50px;

  display: flex;
  align-items: center;
  justify-content: center;
`
export const ContainerLeft = styled(ContainerRight)`
  background-color: #006fd1;
`

export const Feature = styled(Container)`
  color: white;
  border-color: white;
  text-align: center;
`
export const Title = styled.h1`
  font-size: 50px;
`
export const SubTitle = styled.p``
