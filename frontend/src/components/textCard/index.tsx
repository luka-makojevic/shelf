import ReactMarkdown from 'react-markdown';
import { PlainText, Title } from '../text/text-styles';
import { Body, CardWrapper, Footer, Header } from './textcard-styles';
import { TextCardProps } from '../../interfaces/types';

const TextCard = ({ title, text, subtitle }: TextCardProps) => (
  <CardWrapper>
    <Header>
      <Title>{title}</Title>
      <PlainText>{subtitle}</PlainText>
    </Header>
    <Body>
      <ReactMarkdown>{text}</ReactMarkdown>,
    </Body>
    <Footer>
      <PlainText>Than you for reading</PlainText>
    </Footer>
  </CardWrapper>
);

export default TextCard;
