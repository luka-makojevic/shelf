import styled from 'styled-components';
import { theme } from '../../theme';
import { ButtonProps } from '../UI/button/button.interfaces';
import { AlertStyleProp } from './alert.interfaces';

export const AlertContainer = styled.div<AlertStyleProp>`
  position: absolute;
  top: 10px;
  right: 10px;
  display: flex;
  height: 100px;
  width: 70%;
  max-width: 500px;
  border-radius: 10px;
  box-shadow: 0px 0px 8px 0px ${theme.colors.shadow};
  color: ${({ type }) => {
    if (type === 'error') {
      return theme.colors.danger;
    }
    if (type === 'success') {
      return theme.colors.success;
    }
    return theme.colors.info;
  }};

  border: 1px solid
    ${({ type }) => {
      if (type === 'error') {
        return theme.colors.danger;
      }
      if (type === 'success') {
        return theme.colors.success;
      }
      return theme.colors.info;
    }};
  background-color: white;
`;

export const ContentContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
`;

export const Title = styled.div`
  margin-bottom: ${theme.space.sm};
  font-size: ${theme.fontSizes.lg};
`;

export const Message = styled.div`
  font-size: ${theme.fontSizes.sm};
`;
export const CloseContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-right: ${theme.space.md};
`;

export const Close = styled.button<ButtonProps>`
  background-color: transparent;
  border: none;
`;
