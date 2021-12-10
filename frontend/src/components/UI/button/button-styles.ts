import styled, { css } from 'styled-components';
import { variant, space, border } from 'styled-system';
import { Link as ReachRouterLink } from 'react-router-dom';
import { ButtonStyleProps } from '../../../interfaces/styles';

export const StyledButton = styled('button')<ButtonStyleProps>`
  appearance: none;
  font-family: inherit;
  border: none;
  border-radius: 999px;
  padding: 8px 16px;
  margin: 8px 0;
  font-size: ${({ theme }) => theme.fontSizes[2]};
  cursor: pointer;
  min-width: 90px;

  &:disabled,
  &[disabled] {
    background-color: ${({ theme }) => theme.colors.secondary};
    color: ${({ theme }) => theme.colors.shadow};
  }

  ${(props) =>
    props.size === 'large' &&
    css`
      padding: 10px 20px;
      font-size: ${({ theme }) => theme.fontSizes[3]};
    `}
  ${(props) =>
    props.size === 'small' &&
    css`
      padding: 7px 14px;
      font-size: ${({ theme }) => theme.fontSizes[2]};
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
      primaryBordered: {
        color: 'white',
        bg: 'primary',
        border: '1px solid',
        borderColor: 'white',
      },
      lightBordered: {
        color: 'primary',
        bg: 'white',
        border: '1px solid',
        borderColor: 'primary',
      },
    },
  })};
  ${space}
  ${border}
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
  font-size: ${({ theme }) => theme.fontSizes[2]};
  text-decoration: none;
  text-align: center;
  cursor: pointer;
  ${(props) =>
    props.size === 'large' &&
    css`
      padding: 10px 20px;
      font-size: ${({ theme }) => theme.fontSizes[3]};
    `}
  ${(props) =>
    props.size === 'small' &&
    css`
      padding: 7px 14px;
      font-size: ${({ theme }) => theme.fontSizes[2]};
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
