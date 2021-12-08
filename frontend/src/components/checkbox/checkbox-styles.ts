import styled from 'styled-components';
import { theme } from '../../theme';

export const CheckboxWrapper = styled.div`
  display: flex;
  align-items: center;
`;

export const Checkbox = styled.input`
  cursor: pointer;
  background-color: red;
`;

export const Label = styled.label`
  font-size: ${theme.fontSizes.md};
  cursor: pointer;
`;
