import styled from 'styled-components';
import { theme } from '../../theme';

export const Backdrop = styled.div`
  width: 100vw;
  height: 100vh;
  position: fixed;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  background: rgba(46, 52, 66, 0.6);
`;

export const ModalContainer = styled.div`
  width: 500px;
  height: 500px;
  position: absolute;

  display: flex;

  background-color: ${theme.colors.primary};
  box-shadow: 0 0 8px 0 ${theme.colors.danger};
`;
