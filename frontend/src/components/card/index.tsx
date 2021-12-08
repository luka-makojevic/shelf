import { CardWrapper, IconWrapper, Image, Text } from './card-styles';
import { CardProps } from '../../utils/interfaces/propTypes';

const Card = ({ image, text, alt }: CardProps) => (
  <CardWrapper>
    <IconWrapper>
      <Image src={image} alt={alt} />
    </IconWrapper>
    <Text>{text}</Text>
  </CardWrapper>
);

export default Card;
