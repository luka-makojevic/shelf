import styled from 'styled-components';
import { Link as ReachRouterLink } from 'react-router-dom';
import { space, layout } from 'styled-system';
import { HeaderProps, ProfileProps } from '../../interfaces/styles';

export const Container = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-height: 100px;
  width: 100%;
  position: absolute;
  padding: 15px 20px 0 20px;
`;

export const Link = styled(ReachRouterLink)``;

export const Logo = styled.img<HeaderProps>`
  width: 70px;
  ${layout}
  ${space}
`;
export const DropDown = styled.div`
  position: absolute;
  display: none;
  background: white;
  border: 1px solid black;
  justify-content: center;
  align-items: center;
  top: 48px;
  right: 0px;
  padding: 10px;
  width: 120px;
  height: 80px;
  border-radius: 20px;
`;

export const Profile = styled.div<ProfileProps>`
  width: 50px;
  height: 50px;
  border-radius: 100px;
  border: 2px solid #8ea2d8;
  color: #8ea2d8;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  cursor: pointer;
  user-select: none;
  background: white;
  ${({ hideProfile }) => hideProfile && 'display:none;'}
  &:hover > ${DropDown} {
    display: flex;
    cursor: default;
  }
`;
