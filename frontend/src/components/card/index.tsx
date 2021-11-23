import { CardWrapper, IconWrapper, Image, Text } from './card-styles';

const Card = ({ image, text }: { image: string; text: string }) => {
  return (
    <CardWrapper>
      <IconWrapper>
        <Image src={image} alt="" />
      </IconWrapper>
      <Text>{text}</Text>
    </CardWrapper>
  );
};

export default Card;
