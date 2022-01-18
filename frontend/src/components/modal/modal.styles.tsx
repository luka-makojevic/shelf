import styled from 'styled-components';
import { theme } from '../../theme';
import { ButtonProps } from '../UI/button/button.interfaces';
import { FunctionStyleProps, ModalStyleProps } from './modal.interfaces';

export const Backdrop = styled.div`
  width: 100vw;
  height: 100vh;
  position: fixed;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  background: rgba(46, 52, 66, 0.6);

  z-index: 9999;
`;

export const ModalContainer = styled.div`
  min-width: 300px;
  max-width: 500px;
  position: absolute;

  background-color: ${theme.colors.white};
  border-radius: 10px;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

export const Header = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;

  max-height: 70px;
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
  padding-top: 0;

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
  cursor: pointer;
`;

export const DeleteModalBody = styled.div`
  display: flex;
  flex-direction: column;
`;

export const RadioContainer = styled.div`
  display: flex;

  @media (max-width: ${theme.breakpoints.md}) {
    flex-direction: column;
  }
`;

export const RadioLabel = styled.label<FunctionStyleProps>`
  color: black;
  cursor: pointer;
  background: ${theme.colors.white};
  border-radius: 30px;
  border: 1px;
  width: 100%;
  height: 200px;
  margin-right: ${theme.space.lg};
  &:last-of-type {
    margin-right: 0;
  }
  padding: ${theme.space.sm} ${theme.space.lg};
  img {
    width: 45px;
    height: 45px;
  }
  input {
    cursor: pointer;
  }

  @media (max-width: ${theme.breakpoints.md}) {
    margin-bottom: ${theme.space.sm};
  }
`;

export const RadioInner = styled.div`
  padding: ${theme.space.sm} 0;
  display: flex;
  justify-content: space-between;
  min-height: 120px;
`;
export const FunctionModalContainer = styled.div`
  background: ${theme.colors.secondary};

  height: 700px;
  color: white;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  border-radius: 10px;
  margin: ${theme.space.md};
`;
export const FunctionContainer = styled.div`
  overflow-y: auto;
  padding: 0px ${theme.space.lg} ${theme.space.sm};
  border-radius: 55px;
  height: 100%;
`;
export const RadioTitle = styled.p``;
export const RadioSubTitle = styled.p`
  margin: 0;
  font-size: ${theme.fontSizes.sm};
`;

export const FunctionHeader = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;

  height: 70px;
  padding: ${theme.space.none} ${theme.space.sm};

  width: 100%;
`;
