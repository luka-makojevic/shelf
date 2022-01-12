import styled from 'styled-components';
import { theme } from '../../theme';
import {
  HeaderContainerProps,
  HeaderStyleProps,
  ProfilePictureProps,
  ProfileProps,
} from './header.interface';

export const HeaderContainer = styled.div<HeaderContainerProps>`
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-height: 100px;
  width: 100%;
  ${({ position }) => position && `position:${position};`};
  padding: 15px 20px 0 20px;
  @media (max-width: ${theme.breakpoints.md}) {
    position: static;
  }
`;

export const Logo = styled.img<HeaderStyleProps>`
  width: 70px;
`;

export const ProfileInfoWrapper = styled.div`
  margin-left: ${theme.space.md};
`;

export const DropDown = styled.div`
  position: absolute;
  display: flex;
  background: white;
  border: 1px solid black;
  justify-content: space-between;
  align-items: center;
  top: 48px;
  right: 0px;
  padding: ${theme.space.md};
  min-width: 200px;
  height: 140px;
  border-radius: 20px;
`;
export const ProfilePicture = styled.div<ProfilePictureProps>`
  border: 2px solid #006fd1;
  border-radius: 999px;
  width: 45px;
  height: 45px;
  background: ${({ imgUrl }) => `url('${imgUrl}')`};
  background-size: contain;
  background-repeat: no-repeat;
  cursor: pointer;
`;

export const ProfileContainer = styled.div<ProfileProps>`
  position: relative;
  ${({ hideProfile }) => hideProfile && 'display:none;'}
`;
