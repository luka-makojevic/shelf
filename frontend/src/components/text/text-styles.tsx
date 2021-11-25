import styled from 'styled-components';
import { space, typography, color, border } from 'styled-system';
import { Link as ReachRouterLink } from 'react-router-dom';
import { TextProps } from '../../interfaces/styles';

export const Title = styled.h1<TextProps>`
  /* height: 50%; */
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
  font-weight: ${({ theme }) => theme.fontWeights.bold};
  ${space}
  ${typography}
  ${color}
  ${border}
`;

export const PlainText = styled.p<TextProps>`
  font-size: 13px;
`;

export const Error = styled.p`
  color: red;
  width: 100%;
  text-align: center;
  font-size: 10px;
`;

export const PlainText = styled.p<TextProps>`
  font-size: 13px;
  ${typography}
  ${color}
`;
