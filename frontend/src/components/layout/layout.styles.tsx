import styled from 'styled-components';
import { layout, flexbox, space, color, grid, border } from 'styled-system';
import { ContainerProps } from '../../interfaces/styles';

export const Wrapper = styled.div<ContainerProps>`
  display: flex;
  width: 100vw;
  height: 100vh;
  ${space}
  ${layout}
  ${flexbox}
`;
export const Container = styled.div<ContainerProps>`
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  ${space}
  ${layout}
  ${flexbox}
  ${color}
  ${grid}
`;
export const Holder = styled.div<ContainerProps>`
  ${space}
  ${layout}
  ${flexbox}
  ${color}
  ${grid}
  ${border}
`;
export const Feature = styled.div<ContainerProps>`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin: 0 ${({theme}) => theme.space[3]};
  padding: ${({theme}) => theme.space[3]};
  height: 100%;
  max-width: 400px;
  max-height: 450px;
  text-align: center;
  color: white;
  border: 1px solid white;
  border-radius: 50px;

  ${space}
  ${layout}
  ${flexbox}
  ${color}
`;
