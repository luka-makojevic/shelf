import styled from 'styled-components';
import { theme } from '../../theme';

export const NavigationSidebarOuterWrapper = styled.div`
  display: flex;
  height: 100%;
  @media (max-width: ${theme.breakpoints.md}) {
    position: absolute;
  }
`;

export const NavigationSidebarInnerWrapper = styled.div`
  position: relative;
  height: 100%;
  z-index: 1;
`;

export const NavigationSidebarContentWrapper = styled.nav`
  width: 280px;
  height: 100%;
  background: ${theme.colors.primary};
`;

export const NavigationSidebarToggleButton = styled.button`
  position: absolute;
  left: 100%;
  background: none;
  border: none;
  padding: 0;
  color: ${theme.colors.primary};
  font-size: ${theme.fontSizes.xl};
  cursor: pointer;

  span {
    display: none;
    position: absolute;
    top: 0px;
    left: ${theme.space.lg};

    background-color: ${theme.colors.white};
    border: 1px solid ${theme.colors.primary};
    font-size: ${theme.fontSizes.sm};

    padding: ${theme.space.xs};
  }

  &:hover span {
    display: block;
  }
`;
