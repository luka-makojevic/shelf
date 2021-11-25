import styled from 'styled-components';
import { border, color, typography, layout, space } from 'styled-system';
import { ButtonProps } from '../../../interfaces/styles';

export const StyledButton = styled.button<ButtonProps>`
  border-radius: 999px;
  border: none;
  color: white;
  padding: 5px 10px;
  ${border}
  ${color}
  ${typography}
  ${layout}
  ${space}
`;
