import styled from 'styled-components';
import { theme } from '../../theme';

export const TableContainer = styled.div`
  padding: ${theme.space.lg};
  box-shadow: 0 0 6px 0 ${theme.colors.shadow};
  width: 100%;
  // regulates long text if it's inserted into the tableWrapper as child
  word-wrap: break-word;
  white-space: pre-wrap;
  word-break: break-word;
`;

export const TableTitle = styled.div`
  font-size: ${theme.fontSizes.xl};
`;

export const TableDescription = styled.div`
  font-size: ${theme.fontSizes.md};
`;

export const TableContent = styled.div`
  display: flex;
  flex-direction: column;

  margin-top: ${theme.space.lg};
`;
