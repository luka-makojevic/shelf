import { CardWrapper, IconWrapper, Image, Text } from './card-styles';
import { CardProps } from '../../interfaces/types';

const Card = ({ image, text, alt }: CardProps) => (
  <CardWrapper>
    <IconWrapper>
      <Image src={image} alt={alt} />
    </IconWrapper>
    <Text>{text}</Text>
  </CardWrapper>
);

export default Card;
