import styled from 'styled-components';
import { theme } from '../../../theme';
import { DropZoneWrapperProps } from './uploadModal.interfaces';

export const DropZoneWrapper = styled.div<DropZoneWrapperProps>`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  overflow-y: auto;
  max-height: 300px;
  width: 100%;
  min-height: 260px;
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

export const AddedFilesList = styled.ul`
  margin: ${theme.space.md} 0;
  padding: 0;
  width: 100%;
  list-style: none;
  font-size: ${theme.fontSizes.xs};
`;

export const AddedFilesListItem = styled.li`
  display: flex;
  justify-content: space-between;
  padding: ${theme.space.xs};
  border-bottom: 1px solid ${theme.colors.primary};
`;

export const AddedFilesIconButton = styled.button`
  background: none;
  border: none;
  padding: 0;
  color: ${theme.colors.primary};
  cursor: pointer;
`;
