import styled from 'styled-components';

export const CardWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: justify;
  position: relative;
  max-width: 700px;
  margin: ${({theme}) => theme.space[4]};
  background-color: ${({ theme }) => theme.colors.primary};
  color: white;
  border-radius: 30px;
  box-shadow: 0px 0px 13px 1px ${({ theme }) => theme.colors.secondary};
`;

export const Header = styled.div`
  padding: ${({theme}) => theme.space[2]} ${({theme}) => theme.space[3]};
  border-bottom: 1px solid white;
  width: 100%;
`;

export const Body = styled.div`
  padding: 0 ${({theme}) => theme.space[4]};
  width: 100%;
  height: 100%;
  overflow-y: scroll;
  font-size: ${({ theme }) => theme.fontSizes[1]};
`;

export const Footer = styled.div`
  bottom: 0;
  padding: ${({theme}) => theme.space[2]} ${({theme}) => theme.space[3]};
  height: 100px;
  width: 100%;
  border-top: 1px solid white;
`;
