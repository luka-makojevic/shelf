import { StyledButton } from './button-styles';

export const Button = ({ children, ...restProps }: any) => {
  return <StyledButton {...restProps}>{children}</StyledButton>;
};
