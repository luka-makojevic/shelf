import styled from 'styled-components'

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

export const Feature = styled.div`
  max-width: 400px;
  min-height: 450px;
  padding: 40px 20px;
  text-align: center; 
  color: white;
  border: 2px solid white;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;


`
export const Title = styled.h1`
  font-size: 50px;
`
export const SubTitle = styled.p``
