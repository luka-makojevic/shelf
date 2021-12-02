import styled from 'styled-components';
import { space, typography, color, border } from 'styled-system';
import { Link as ReachRouterLink } from 'react-router-dom';
import { TextProps } from '../../interfaces/styles';

export const Title = styled.h1<TextProps>`
  ${space}
  ${typography}
  ${color}
`;
export const SubTitle = styled.p<TextProps>`
  ${space}
  ${typography}
  ${color}
`;
export const AccentText = styled.p<TextProps>`
  color: ${({ theme }) => theme.colors.primary};
  ${space}
  ${typography}
  ${color}
`;
export const Link = styled(ReachRouterLink)<TextProps>`
  color: ${({ theme }) => theme.colors.primary};
  text-decoration: none;
  ${space}
  ${typography}
  ${color}
  ${border}
`;

export const Error = styled.p`
  margin: 0;
  margin-left: ${({theme}) => theme.space[2]};
  color: ${({ theme }) => theme.colors.danger};
  font-size: ${({ theme }) => theme.fontSizes[0]};
`;

export const Success = styled.p`
  width: 100%;
  text-align: center;
  color: ${({ theme }) => theme.colors.success};
  font-size: ${({ theme }) => theme.fontSizes[0]};
`;

export const PlainText = styled.p<TextProps>`
  font-size: ${({ theme }) => theme.fontSizes[1]};
  margin: ${({theme}) => theme.space[1]} 0;
  ${typography}
  ${color}
`;
