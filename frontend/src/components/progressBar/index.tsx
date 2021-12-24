import { ProgressBarProps } from './progressBar.interfaces';
import { ProgressBarWrapper, StyledProgress } from './progressBar.styles';

const ProgressBar = ({ progress }: ProgressBarProps) => (
  <ProgressBarWrapper>
    <StyledProgress progress={progress}>{progress}%</StyledProgress>
  </ProgressBarWrapper>
);

export default ProgressBar;
