import styled from 'styled-components'

export const CardWrapper = styled.div`
  width: 200px;
  display: flex;
  flex-direction: column;
  justify-self: center;
  align-items: center;
  text-align: center;

  p {
    font-size: 12px;
  }
`
export const IconWrapper = styled.div`
  width: 100%;
  height: 200px;
  padding: 40px;
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
