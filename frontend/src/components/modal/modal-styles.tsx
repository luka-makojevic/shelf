import styled from 'styled-components';
import { theme } from '../../theme';
import { ButtonProps } from '../UI/button/button.interfaces';

export const Backdrop = styled.div`
  width: 100vw;
  height: 100vh;
  position: fixed;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  background: rgba(46, 52, 66, 0.6);
`;

export const ModalContainer = styled.div`
  width: 500px;
  position: absolute;

  background-color: ${theme.colors.white};
  border-radius: 10px;
  box-shadow: 0 0 8px 0 ${theme.colors.shadow};

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

export const Header = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;

  height: 70px;
  padding: ${theme.space.none} ${theme.space.sm};

  width: 100%;
  color: ${theme.colors.primary};
`;

export const HeaderItem = styled.div`
  margin: 0 ${theme.space.md};
`;

export const Body = styled.div`
  height: 100%;
  width: 100%;

  padding: ${theme.space.md};

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  color: ${theme.colors.primary};
`;

export const Footer = styled.div`
  padding: ${theme.space.none} ${theme.space.sm};
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: end;
  bottom: 0;
  height: 90px;
  width: 100%;
  border-top: 1px solid ${theme.colors.primary};
`;

export const Close = styled.button<ButtonProps>`
  background-color: transparent;
  border: none;
  padding: ${theme.space.none};
`;
