import ReactMarkdown from 'react-markdown';
import { PlainText, H2 } from '../text/text-styles';
import { Body, CardWrapper, Footer, Header } from './textcard-styles';
import { TextCardProps } from '../../utils/interfaces/propTypes';

const TextCard = ({ title, text, subtitle }: TextCardProps) => (
  <CardWrapper>
    <Header>
      <H2>{title}</H2>
      <PlainText>{subtitle}</PlainText>
    </Header>
    <Body>
      <ReactMarkdown>{text}</ReactMarkdown>
    </Body>
    <Footer>
      <PlainText>Thank you for reading</PlainText>
    </Footer>
  </CardWrapper>
);

export default TextCard;
