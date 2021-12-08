import styled, { css } from 'styled-components';
import { Link as ReachRouterLink } from 'react-router-dom';
import { ButtonStyleProps } from '../../../utils/interfaces/styles';
import { theme } from '../../../theme';

export const StyledButton = styled('button')<ButtonStyleProps>`
  appearance: none;
  font-family: inherit;
  border: none;
  border-radius: 999px;
  padding: 8px 16px;
  margin: 8px 0;
  font-size: ${theme.fontSizes[2]};
  cursor: pointer;
  min-width: 90px;
  ${(props) =>
    props.size === 'large' &&
    css`
      padding: 10px 20px;
      font-size: ${theme.fontSizes[3]};
    `}
  ${(props) =>
    props.size === 'small' &&
    css`
      padding: 7px 14px;
      font-size: ${theme.fontSizes[2]};
    `}
  ${(props) =>
    props.fullwidth &&
    css`
      width: 100%;
    `}

    ${({ variant }) => {
    switch (variant) {
      case 'primary':
        return css`
          color: white;
          background: ${theme.colors.primary};
        `;
      case 'secondary':
        return css`
          color: white;
          background: ${theme.colors.secondary};
        `;
      case 'light':
        return css`
          color: ${theme.colors.primary};
          background: white;
        `;
      default:
        return null;
    }
  }}
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
  font-size: ${theme.fontSizes[2]};
  text-decoration: none;
  text-align: center;
  cursor: pointer;
  ${(props) =>
    props.size === 'large' &&
    css`
      padding: 10px 20px;
      font-size: ${theme.fontSizes[3]};
    `}
  ${(props) =>
    props.size === 'small' &&
    css`
      padding: 7px 14px;
      font-size: ${theme.fontSizes[2]};
    `}
    

  ${(props) =>
    props.fullwidth &&
    css`
      width: 100%;
    `}    
    ${({ variant }) => {
    switch (variant) {
      case 'primary':
        return css`
          color: white;
          background: ${theme.colors.primary};
        `;
      case 'secondary':
        return css`
          color: white;
          background: ${theme.colors.secondary};
        `;
      case 'light':
        return css`
          color: ${theme.colors.primary};
          background: white;
        `;
      default:
        return null;
    }
  }}
`;

export const IconButton = styled(StyledButton)`
  img {
    width: 20px;

    margin-right: 10px;
  }
  display: flex;
  align-items: center;
`;
