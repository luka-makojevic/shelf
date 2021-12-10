import styled from 'styled-components';
import { InputProps } from '../../interfaces/styles';
import { theme } from '../../theme';

export const Input = styled.input<InputProps>`
  width: 100%;
  min-height: 45px;
  border: 1px solid ${theme.colors.secondary};
  border-radius: 30px;
  text-indent: 10px;
  margin: 5px 0;

  &:focus {
    outline: none;
    box-shadow: 0 0 6px 0 ${theme.colors.secondary};
  }
`;

export const InputContainer = styled.div`
  position: relative;
  width: 100%;
  min-height: 75px;
`;

export const SeenIcon = styled.img`
  position: absolute;
  width: 20px;
  height: 20px;
  right: 12px;
  top: 18px;
`;
