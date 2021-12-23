import styled from 'styled-components';
import { theme } from '../../../theme';
import { DropZoneWrapperProps } from './uploadModal.interfaces';

export const DropZoneWrapper = styled.div<DropZoneWrapperProps>`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  background: ${({ isDragOver }) =>
    isDragOver ? `${theme.colors.primary}55` : 'transparent'};
  padding: ${theme.space.lg};
  border: 4px dashed ${theme.colors.primary};
  transition: all 500ms;
`;

export const AddFilesLabel = styled.label`
  border: 1px solid ${theme.colors.primary};
  border-radius: ${theme.space.sm};
  padding: ${theme.space.sm} ${theme.space.md};
`;

export const AddFilesInput = styled.input`
  position: absolute;
  visibility: hidden;
`;

export const AddFilesText = styled.p`
  margin: 0;
  margin-bottom: ${theme.space.sm};
`;

export const AddedFilesText = styled.p`
  margin: 0;
  width: 100%;
  margin-top: ${theme.space.xs};
  font-size: ${theme.fontSizes.xs};
`;
