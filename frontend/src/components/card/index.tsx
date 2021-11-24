import { CardWrapper, IconWrapper, Image, Text } from './card-styles';

type CardProps = { image: string; text: string };

const Card = ({ image, text }: CardProps) => (
  <CardWrapper>
    <IconWrapper>
      <Image src={image} alt="" />
    </IconWrapper>
    <Text>{text}</Text>
  </CardWrapper>
);

export default Card;
