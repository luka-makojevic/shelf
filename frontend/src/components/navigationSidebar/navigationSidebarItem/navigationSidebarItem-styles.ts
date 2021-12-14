import { Link } from 'react-router-dom';
import styled from 'styled-components';
import { theme } from '../../../theme';

export const Item = styled.div`
  display: flex;
  align-items: center;
  color: ${theme.colors.white};
  user-select: none;
`;

export const ItemWrapper = styled.div`
  display: flex;
  justify-content: space-between;

  align-items: center;
  padding: ${theme.space.sm} ${theme.space.md};
  color: ${theme.colors.white};
`;

export const ItemChildrenWrapper = styled.div`
  padding-left: ${theme.space.md};
  border-bottom: 1px solid ${theme.colors.white};
`;

export const ItemIconButton = styled.button`
  background: none;
  border: none;
  padding: 0;
  color: ${theme.colors.white};
  font-size: ${theme.fontSizes.lg};
  cursor: pointer;
`;

export const NavigationSidebarLink = styled(Link)`
  text-decoration: none;
  color: ${theme.colors.white};
  margin-left: ${theme.space.sm};
`;
