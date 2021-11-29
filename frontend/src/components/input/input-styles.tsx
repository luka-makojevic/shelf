import styled from 'styled-components';
import { space, border } from 'styled-system';
import { FormProps } from '../../interfaces/styles';

export const Input = styled.input<FormProps>`
  width: 100%;
  min-height: 45px;
  padding: 5px;
  border: 1px solid ${({ theme }) => theme.colors.secondary};
  border-radius: 30px;
  text-indent: 10px;
  margin: 5px 0;
  ${space}
  ${border}

&:focus {
    outline: none;
    box-shadow: 0 0 6px 0 ${({ theme }) => theme.colors.secondary};
  }
`;

export const InputContainer = styled.div`
  position: relative;
  height: 70px;
`;

export const PasswordContainer = styled.div`
  position: relative;
  width: 100%;
`;

export const SeenIcon = styled.img`
  position: absolute;
  width: 20px;
  height: 20px;
  right: 12px;
  top: 18px;
`;
