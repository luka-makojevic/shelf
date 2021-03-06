import styled, { css } from 'styled-components';
import { Link as ReachRouterLink } from 'react-router-dom';
import { theme } from '../../../theme';
import { ButtonStyleProps } from './button.interfaces';

export const StyledButton = styled('button')<ButtonStyleProps>`
  appearance: none;
  font-family: inherit;
  border: none;
  border-radius: 999px;
  padding: ${theme.space.sm} ${theme.space.md};
  margin: 8px 0;
  font-size: ${theme.fontSizes.md};
  cursor: pointer;
  min-width: 90px;

  &:disabled,
  &[disabled] {
    background-color: ${theme.colors.secondary};
    color: ${theme.colors.primary};
  }

  ${(props) =>
    props.size === 'large' &&
    css`
      padding: 10px 20px;
      font-size: ${theme.fontSizes.lg};
    `}
  ${(props) =>
    props.size === 'small' &&
    css`
      padding: ${theme.space.xs} ${theme.space.sm};
      font-size: ${theme.fontSizes.md};
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
      case 'lightBordered':
        return css`
          color: ${theme.colors.primary};
          background: white;
          border: 1px solid ${theme.colors.primary};
        `;
      case 'primaryBordered':
        return css`
          color: ${theme.colors.white};
          background: ${theme.colors.primary};
          border: 1px solid ${theme.colors.white};
          &:hover {
            background-color: rgba(255, 255, 255, 0.2);
          }
        `;
      default:
        return null;
    }
  }}

  @media(max-width: ${theme.breakpoints.sm}) {
    width: 90%;
  }
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
  padding: ${theme.space.sm} ${theme.space.md};
  font-size: ${theme.fontSizes.md};
  text-decoration: none;
  text-align: center;
  cursor: pointer;
  ${(props) =>
    props.size === 'large' &&
    css`
      padding: 10px 20px;
      font-size: ${theme.fontSizes.lg};
    `}
  ${(props) =>
    props.size === 'small' &&
    css`
      padding: ${theme.space.sm} ${theme.space.md};
      font-size: ${theme.fontSizes.md};
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
  svg {
    width: 20px;
    margin-right: 5px;
  }

  @media (max-width: ${theme.breakpoints.sm}) {
    svg {
      width: 100%;
      font-size: ${theme.fontSizes.lg};
    }
    span {
      display: none;
    }
  }
`;

export const IconButtonContainer = styled.div`
  display: flex;
  align-items: center;
`;
