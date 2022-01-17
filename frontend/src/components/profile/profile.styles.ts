import styled from 'styled-components';
import { theme } from '../../theme';

export const ProfileWrapper = styled.div`
  display: flex;

  @media (max-width: ${theme.breakpoints.sm}) {
    flex-direction: column;
    height: auto;
    align-items: center;
  }
`;

export const ProfileLeft = styled.div`
  background-color: ${theme.colors.primary};
  padding: ${theme.space.md};
  border-radius: ${theme.space.lg} 0 0 ${theme.space.lg};
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  width: 50%;
  min-width: 300px;

  @media (max-width: ${theme.breakpoints.sm}) {
    border-radius: ${theme.space.lg} ${theme.space.lg} 0 0;
  }
`;

export const ProfileRight = styled.div`
  padding: ${theme.space.md};
  border: 1px solid ${theme.colors.secondary};
  border-radius: 0 ${theme.space.lg} ${theme.space.lg} 0;
  display: flex;
  flex-direction: column;
  width: 50%;
  min-width: 300px;

  @media (max-width: ${theme.breakpoints.sm}) {
    border-radius: 0 0 ${theme.space.lg} ${theme.space.lg};
    padding: ${theme.space.sm};
  }
`;

export const ProfileImageContainer = styled.div`
  position: relative;
`;

export const ProfileImage = styled.img`
  width: ${theme.space.xxl};
  height: ${theme.space.xxl};
  border-radius: 999px;
  border: 3px solid white;
`;

export const EditImageContainer = styled.div`
  position: absolute;
  right: -5px;
  bottom: -5px;
`;

export const About = styled.div`
  margin-top: ${theme.space.lg};
`;

export const ButtonWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
`;
