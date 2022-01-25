import styled from 'styled-components';
import { theme } from '../../theme';
import { ContainerProps } from './layout.interface';

export const Wrapper = styled.div<ContainerProps>`
  display: flex;
  justify-content: center;
  align-items: center;

  height: 100vh;

  @media (max-width: ${theme.breakpoints.md}) {
    flex-direction: column;
    height: auto;
  }
`;
export const GridContainer = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;

  gap: 50px;
  @media (max-width: ${theme.breakpoints.sm}) {
    grid-template-columns: 1fr;
  }
`;

export const LandinPageContainer = styled.div<ContainerProps>`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  padding: ${theme.space.lg};
`;

export const Container = styled.div<ContainerProps>`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  width: 100%;
  padding: ${theme.space.lg};
  background-color: ${({ background }) =>
    background === 'primary' && theme.colors.primary};
  @media (max-width: ${theme.breakpoints.md}) {
    ${({ isHiddenOnSmallScreen }) =>
      isHiddenOnSmallScreen && 'display: none;'}/* height: 100vh;s */
  }
  @media (max-width: ${theme.breakpoints.sm}) {
    height: 100%;
  }
`;

export const EmailVerificationContainer = styled.div<ContainerProps>`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
  padding: ${theme.space.xl};
  border-radius: 30px;
  max-width: 500px;
  width: 100%;
  color: white;
  background-color: ${theme.colors.primary};
`;

export const Feature = styled.div<ContainerProps>`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin: 0 ${theme.space.md};
  padding: ${theme.space.md};
  height: 100%;
  max-width: 400px;
  max-height: 450px;
  text-align: center;
  color: white;
  border: 1px solid white;
  border-radius: 50px;
`;

export const ModalButtonDivider = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
`;

export const DashboardLayout = styled.div`
  display: flex;
  height: calc(100vh - 60px);
`;

export const DashboardContentWrapper = styled.div`
  height: 100%;
  width: 100%;
  padding: ${theme.space.lg};
`;

export const FunctionWrapper = styled.div`
  display: flex;
  margin-top: ${theme.space.md};
`;

export const FunctionEditorWrapper = styled.div`
  flex: 4;
  border: 1px solid lightgray;
`;

export const FunctionResultWrapper = styled.div`
  flex: 3;
  margin-left: ${theme.space.md};
  padding-left: ${theme.space.sm};
  border: 1px solid lightgray;
`;

export const FunctionInfo = styled.div`
  display: flex;
  justify-content: space-between;
  flex: 1;
  margin-left: ${theme.space.md};
  padding-left: ${theme.space.sm};
`;

export const FunctionButtonWrapper = styled.div`
  display: flex;
  justify-content: flex-end;
`;
