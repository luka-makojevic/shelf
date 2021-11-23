import styled from 'styled-components';
import { Link as ReachRouterLink } from 'react-router-dom';
import { space, layout } from 'styled-system';
import { HeaderProps, ProfileProps } from '../../interfaces/styles';

export const Container = styled.div`
  display: flex;
  justify-content: space-between;
  max-height: 100px;
  width: 100%;
  position: absolute;

  padding: 20px 15px 0 15px;
`;
export const Link = styled(ReachRouterLink)``;
export const Logo = styled.img<HeaderProps>`
  ${layout}
  ${space}
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
  cursor: pointer;
  user-select: none;
  ${({ hideProfile }) => hideProfile && 'display:none;'}
`;

export const Top = styled.div``;
