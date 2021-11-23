import { Link } from 'react-router-dom';
import styled from 'styled-components';

export const Container = styled.div``;

export const Top = styled.div`
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0 40px;
  height: 80px;
`;

export const NavLink = styled(Link)`
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
`;

export const Content = styled.div`
  display: flex;
  padding-top: 100px;

  @media screen and (max-width: 768px) {
    flex-direction: column;
  }
`;

export const Logo = styled.img`
  width: 200px;
`;

export const Title = styled.h1`
  font-size: 80px;

  @media screen and (max-width: 768px) {
    font-size: 48px;
  }
`;

export const Text = styled.p``;

export const CallToAction = styled.p`
  margin-top: 80px;
  color: ${({ theme }) => theme.colors.secondary};

  a {
    text-decoration: none;
    color: inherit;
    font-weight: bold;
  }
`;

export const Left = styled.div`
  width: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  padding: 0 80px;

  @media screen and (max-width: 768px) {
    width: 100%;
    padding: 0 20px;
    margin-bottom: 80px;
  }
`;

export const Right = styled.div`
  max-width: 600px;
  width: 50%;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;

  @media screen and (max-width: 768px) {
    width: 100%;
  }

  @media screen and (max-width: 425px) {
    grid-template-columns: 1fr;
  }
`;
