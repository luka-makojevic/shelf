import styled from 'styled-components';
import { theme } from '../../../theme';

export const TooltipWrapper = styled.div`
  background: ${theme.colors.white};
  border: 1px solid ${theme.colors.shadow};
  position: absolute;
  left: 105%;
  top: 80px;
  width: 245px;
  padding: ${theme.space.md};
  border-radius: ${theme.size.sm};
  font-size: ${theme.fontSizes.xs};
  box-shadow: 0px 0px 6px 2px rgba(0, 0, 0, 0.1);

  &:before,
  &:after {
    content: '';
    position: absolute;
    display: block;
  }

  &:before {
    z-index: 2;
    left: -9px;
    top: 16px;
    border-right: 10px solid ${theme.colors.white};
    border-top: 10px solid transparent;
    border-bottom: 10px solid transparent;
  }

  &:after {
    z-index: 1;
    left: -11px;
    top: 15px;
    border-right: 11px solid ${theme.colors.shadow};
    border-top: 11px solid transparent;
    border-bottom: 11px solid transparent;
  }

  @media (max-width: ${theme.breakpoints.md}) {
    top: 130px;
    left: 15%;

    &:before,
    &:after {
      content: '';
      display: none;
    }
  }
`;

export const Text = styled.p`
  margin: 0;
`;

export const List = styled.ul`
  list-style: none;
  padding: 0;
  margin-bottom: 0;
`;

export const ListItem = styled.li`
  &:not(:last-child) {
    margin-bottom: ${theme.space.sm};
  }
`;
