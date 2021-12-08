import styled from 'styled-components';
import { Link as ReachRouterLink } from 'react-router-dom';
import { TextProps } from '../../utils/interfaces/styles';
import { theme } from '../../theme';

export const H1 = styled.h1<TextProps>`
  font-size: ${theme.fontSizes.xxl};
  text-align: center;
`;

export const H2 = styled.h2<TextProps>`
  font-size: ${theme.fontSizes.xl};
  text-align: center;
`;

export const Description = styled.p<TextProps>`
  text-align: center;
`;
export const AccentText = styled.p<TextProps>`
  color: ${theme.colors.primary};
`;
export const Link = styled(ReachRouterLink)<TextProps>`
  color: ${theme.colors.primary};
  text-decoration: none;
`;

export const Error = styled.p`
  margin: 0;
  margin-left: ${theme.space.sm};
  color: ${theme.colors.danger};
  font-size: ${theme.fontSizes.xs};
`;

export const Success = styled.p`
  width: 100%;
  text-align: center;
  color: ${theme.colors.success};
  font-size: ${theme.fontSizes.xs};
`;

export const PlainText = styled.p<TextProps>`
  font-size: ${theme.fontSizes.sm};
  margin: ${theme.space.xs} 0;
`;
