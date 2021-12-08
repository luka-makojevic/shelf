import styled from 'styled-components';

export const CheckboxWrapper = styled.div`
  display: flex;
  align-items: center;
`;

export const Checkbox = styled.input`
  cursor: pointer;
  background-color: red;
`;

export const Label = styled.label`
  font-size: ${({ theme }) => theme.fontSizes[2]};
  cursor: pointer;
`;
