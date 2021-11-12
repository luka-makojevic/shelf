import styled from 'styled-components'

export const Container = styled.div`
  display: flex;
  justify-content: space-between;
  max-height: 100px;
  width: 100%;
  position: absolute;

  padding: 20px 15px 0 15px;
`
export const Logo = styled.img`
  width: 70px;
  height: auto;
  object-fit: contain;
`
export const Profile = styled.div`
  width: 50px;
  height: 50px;

  border-radius: 100px;
  border: 2px solid #8ea2d8;
  color: #8ea2d8;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  user-select: none;
  ${({ theme }) => theme && 'display:none;'}
`
