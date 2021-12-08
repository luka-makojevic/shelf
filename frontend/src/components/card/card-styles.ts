import styled from 'styled-components';
import { theme } from '../../theme';

export const CardWrapper = styled.div`
  width: 200px;
  display: flex;
  flex-direction: column;
  justify-self: center;
  align-items: center;
  text-align: center;
`;

export const Text = styled.p`
  font-size: ${theme.fontSizes[0]};
`;

export const IconWrapper = styled.div`
  width: 100%;
  height: 200px;
  padding: ${theme.space[4]};
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 40px;
  border: 1px solid black;
  box-shadow: 1px 1px 6px ${theme.colors.primary};
`;

export const Image = styled.img`
  object-fit: contain;
  height: 100%;
`;
