import {
  StyledButton,
  StyledLink,
  IconButton,
  SpinnerButton,
} from './button-styles';
import { Spinner } from '../../form/form-styles';
import { ButtonProps } from '../../../interfaces/types';

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
}: ButtonProps) => {
  if (to) {
    return (
      <StyledLink size={size} fullwidth={fullwidth} variant={variant} to={to}>
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
      >
        {icon}
        {children}
      </IconButton>
    );
  }
  return (
    <StyledButton
      size={size}
      onClick={onClick}
      fullwidth={fullwidth}
      variant={variant}
    >
      {children}
    </StyledButton>
  );
};

export const x = 0;
