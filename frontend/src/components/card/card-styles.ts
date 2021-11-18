import styled from 'styled-components'
import { layout, space, typography } from 'styled-system'
import { CardProps } from '../../interfaces/styles'

export const CardWrapper = styled.div`
  max-width: 200px;
  min-width: 100px;
  width: 100%;
  height: 100px;
  display: flex;
  flex-direction: column;
  justify-self: center;
  align-items: center;
  text-align: center;
`
export const Text = styled.p`
  font-size: 12px;
`
export const IconWrapper = styled.div`
  width: 100%;
  height: 200px;
  padding: 40px;
  margin: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 40px;
  border: 1px solid black;
  box-shadow: 1px 1px 6px ${({ theme }) => theme.colors.primary};
  img {
    object-fit: contain;
    height: 100%;
  }
`
