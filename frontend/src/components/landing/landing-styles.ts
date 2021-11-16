import styled from 'styled-components'

// to do

export const Top = styled.div`
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0 40px;
  height: 80px;

  a {
    text-decoration: none;
    margin-left: 20px;
    padding: 10px 20px;
    background-color: ${({ theme }) => theme.colors.primary};
    color: white;
    font-weight: bold;
    border-radius: 999px;

    &:first-child {
      color: ${({ theme }) => theme.colors.primary};
      background-color: transparent;
    }
  }
`

export const Content = styled.div`
  display: flex;
  padding-top: 100px;
`

export const Left = styled.div`
  width: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 0 80px;

  img {
    width: 200px;
  }

  h1 {
    font-size: 80px;
  }

  .call-to-action {
    margin-top: 80px;
    color: ${({ theme }) => theme.colors.secondary};

    a {
      text-decoration: none;
      color: inherit;
      font-weight: bold;
    }
  }
`

export const Right = styled.div`
  max-width: 600px;
  width: 50%;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  row-gap: 40px;
`
