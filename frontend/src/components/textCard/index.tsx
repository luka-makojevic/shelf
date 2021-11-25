import { Logo } from '../header/header-styles';
import { PlainText, Title } from '../text/text-styles';
import { Body, CardWrapper, Footer, Header } from './textcard-styles';

interface TextCardProps {
  title?: string;
  subtitle?: string;
  text?: string;
}

const TextCard = ({ title, text, subtitle }: TextCardProps) => (
  <CardWrapper>
    <Header>
      <Title>{title}</Title>
      <PlainText>{subtitle}</PlainText>
    </Header>
    <Body>
      <PlainText>{text}</PlainText>
    </Body>
    <Footer>
      <PlainText>Than you for reading</PlainText>
    </Footer>
  </CardWrapper>
);

export default TextCard;
