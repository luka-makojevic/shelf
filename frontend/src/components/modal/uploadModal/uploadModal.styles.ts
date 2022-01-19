import styled from 'styled-components';
import { theme } from '../../../theme';
import { DropZoneWrapperProps } from './uploadModal.interfaces';

export const DropZoneWrapper = styled.div<DropZoneWrapperProps>`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  overflow-y: auto;
  min-width: 400px;
  min-height: 200px;
  background: ${({ isDragOver }) =>
    isDragOver ? `${theme.colors.secondary}55` : 'transparent'};
  padding: 0 ${theme.space.md} ${theme.space.md};
  margin-bottom: ${theme.space.md};

  background-image: url("data:image/svg+xml,%3csvg width='100%25' height='100%25' xmlns='http://www.w3.org/2000/svg'%3e%3crect width='100%25' height='100%25' fill='none' stroke='white' stroke-width='2' stroke-dasharray='10%2c 20' stroke-dashoffset='0' stroke-linecap='square'/%3e%3c/svg%3e");
  transition: all 500ms;
`;

export const AddFilesLabel = styled.label`
  border: 1px solid ${theme.colors.white};
  border-radius: ${theme.space.sm};
  padding: ${theme.space.sm} ${theme.space.md};

  &:hover {
    background-color: rgba(255, 255, 255, 0.2);
  }
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
  font-size: ${theme.fontSizes.md};
  max-height: 220px;
  max-width: 400px;
  overflow: scroll;

  &::-webkit-scrollbar {
    display: none;
  }
`;

export const AddedFilesListItem = styled.li`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: ${theme.space.xs};
  margin-bottom: ${theme.space.xs};
  background-color: ${theme.colors.white};
  color: ${theme.colors.primary};
  height: 50px;

  p {
    margin: 0;
    width: 300px;
    overflow: hidden;
    display: inline-block;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  svg {
    margin-right: 10px;
    align-self: center;
  }
`;

export const AddedFilesListItemText = styled.li`
  display: flex;
`;

export const AddedFilesIconButton = styled.button`
  background: none;
  border: none;
  padding-right: 5px;
  color: ${theme.colors.primary};
  cursor: pointer;
`;

// upload picture modal down
export const DropZoneWrapperImage = styled.div<DropZoneWrapperProps>`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  background: ${({ isDragOver }) =>
    isDragOver ? `${theme.colors.secondary}55` : 'transparent'};
  transition: all 500ms;
  margin-top: ${theme.space.md};
  min-height: 250px;
`;

export const ImageWrapper = styled.div`
  position: relative;
  height: 200px;
  max-width: 700px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around;
  text-align: center;
`;

export const RemoveImageIconButton = styled.div`
  position: absolute;
  right: -11px;
  top: -11px;
  display: flex;
  border-radius: 50px;
  padding: 2px;
  cursor: pointer;
  background: ${theme.colors.primary};
`;

export const ModalImage = styled.img`
  height: 100%;
  width: 100%;
  object-fit: contain;
`;
