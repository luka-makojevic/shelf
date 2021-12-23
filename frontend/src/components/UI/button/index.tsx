import {
  StyledButton,
  StyledLink,
  IconButton,
  SpinnerButton,
  IconButtonContainer,
} from './button-styles';
import { Spinner } from '../../form/form-styles';
import { ButtonProps } from './button.interfaces';

export const Button = ({
  children,
  icon,
  to,
  isLoading,
  spinner,
  onClick,
  size,
  variant = 'primary',
  fullwidth,
  ...restProps
}: ButtonProps) => {
  if (to) {
    return (
      <StyledLink
        size={size}
        fullwidth={fullwidth}
        variant={variant}
        to={to}
        {...restProps}
      >
        {children}
      </StyledLink>
    );
  }
  if (spinner) {
    return (
      <SpinnerButton
        size={size}
        onClick={onClick}
        fullwidth={fullwidth}
        variant={variant}
        {...restProps}
      >
        {isLoading ? (
          <Spinner
            src={`${process.env.PUBLIC_URL}/assets/icons/loading.png`}
            alt="loading"
          />
        ) : (
          children
        )}
      </SpinnerButton>
    );
  }
  if (icon) {
    return (
      <IconButton
        size={size}
        onClick={onClick}
        fullwidth={fullwidth}
        variant={variant}
        {...restProps}
      >
        <IconButtonContainer>
          {icon}
          {children}
        </IconButtonContainer>
      </IconButton>
    );
  }
  return (
    <StyledButton
      size={size}
      onClick={onClick}
      fullwidth={fullwidth}
      variant={variant}
      {...restProps}
    >
      {children}
    </StyledButton>
  );
};
