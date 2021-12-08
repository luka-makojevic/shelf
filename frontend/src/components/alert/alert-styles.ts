import styled from 'styled-components';
import { theme } from '../../theme';
import { ButtonProps } from '../../utils/interfaces/propTypes';
import { AlertBoxProp } from '../../utils/interfaces/styles';

export const AlertContainer = styled.div<AlertBoxProp>`
  position: absolute;
  top: 20px;
  right: 20px;
  display: flex;
  width: 500px;
  height: 100px;
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
