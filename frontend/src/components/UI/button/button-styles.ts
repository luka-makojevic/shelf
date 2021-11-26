import styled from 'styled-components';
import { border, color, typography, layout, space } from 'styled-system';
import { ButtonProps } from '../../../interfaces/styles';

export const StyledButton = styled.button<ButtonProps>`
  border-radius: 999px;
  border: none;
  color: white;
  padding: 5px 10px;
  &:hover {
    background: #006fd1;
    box-shadow: 0px 0px 4px 2px rgba(0, 0, 0, 0.1);
  }
  cursor: pointer;
  ${border}
  ${color}
  ${typography}
  ${layout}
  ${space}
`;
