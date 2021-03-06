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

export const ButtonContainer = styled.div`
  button {
    margin-right: 10px;
  }

  @media (max-width: ${theme.breakpoints.sm}) {
    display: grid;
    grid-template-columns: 3fr 3fr;
    justify-items: center;

    button {
      margin-right: 0px;
    }
  }
`;

export const TableDescription = styled.div`
  font-size: ${theme.fontSizes.md};
`;

export const TableContent = styled.div`
  display: flex;
  flex-direction: column;
`;

export const ActionsBox = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;

  margin-top: ${theme.space.lg};
  margin-bottom: ${theme.space.sm};
`;

export const ButtonActionsBox = styled.div`
  display: flex;
  width: 50%;
  justify-content: end;

  button {
    margin-left: ${theme.space.sm};
  }
`;
