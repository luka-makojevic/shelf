import styled from 'styled-components';
import { theme } from '../../theme';
import { ProgressBarProps } from './progressBar.interfaces';

export const ProgressBarWrapper = styled.div`
  width: 100%;
  margin-top: ${theme.space.xs};
  background: ${theme.colors.white};
  border: 1px solid ${theme.colors.primary};
`;

export const StyledProgress = styled.div<ProgressBarProps>`
  align-self: flex-start;
  background: ${theme.colors.primary};
  color: ${theme.colors.white};
  font-size: ${theme.fontSizes.xs};
  text-align: center;
  width: ${({ progress }) => `${progress}%`};
`;
