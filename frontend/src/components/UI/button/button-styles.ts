import styled, { css } from 'styled-components';
import { variant } from 'styled-system';
import { Link as ReachRouterLink } from 'react-router-dom';
import { ButtonStyleProps } from '../../../interfaces/styles';

export const StyledButton = styled('button')<ButtonStyleProps>`
  appearance: none;
  font-family: inherit;
  border: none;
  border-radius: 999px;
  margin: 8px 0;
  padding: 8px 16px;
  font-size: 16px;
  cursor: pointer;
  min-width: 90px;
  ${(props) =>
    props.size === 'large' &&
    css`
      padding: 10px 20px;
      font-size: 18px;
    `}
  ${(props) =>
    props.size === 'small' &&
    css`
      padding: 7px 14px;
      font-size: 15px;
    `}
  ${(props) =>
    props.fullwidth &&
    css`
      width: 100%;
    `}

  ${variant({
    variants: {
      primary: {
        color: 'white',
        bg: 'primary',
      },
      secondary: {
        color: 'white',
        bg: 'secondary',
      },
      light: {
        color: 'primary',
        bg: 'white',
      },
    },
  })};
`;
export const SpinnerButton = styled(StyledButton)`
  padding: 0;
  min-height: 45px;
`;

export const StyledLink = styled(ReachRouterLink)<ButtonStyleProps>`
  appearance: none;
  font-family: inherit;
  border: none;
  border-radius: 999px;
  margin: 8px 0;
  padding: 8px 16px;
  font-size: 16px;
  text-decoration: none;
  text-align: center;
  cursor: pointer;
  ${(props) =>
    props.size === 'large' &&
    css`
      padding: 10px 20px;
      font-size: 18px;
    `}
  ${(props) =>
    props.size === 'small' &&
    css`
      padding: 7px 14px;
      font-size: 15px;
    `}
    

  ${(props) =>
    props.fullwidth &&
    css`
      width: 100%;
    `}

${variant({
    variants: {
      primary: {
        color: 'white',
        bg: 'primary',
      },
      secondary: {
        color: 'white',
        bg: 'secondary',
      },
      light: {
        color: 'primary',
        bg: 'white',
      },
    },
  })};
`;

export const IconButton = styled(StyledButton)`
  img {
    width: 20px;

    margin-right: 10px;
  }
  display: flex;
  align-items: center;
`;
