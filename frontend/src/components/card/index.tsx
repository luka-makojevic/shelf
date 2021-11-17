import { CardWrapper, IconWrapper } from './card-styles'

const Card = ({ image, text }: { image: string; text: string }) => {
  return (
    <CardWrapper>
      <IconWrapper>
        <img src={image} alt="" />
      </IconWrapper>
      <p>{text}</p>
    </CardWrapper>
  )
}

export default Card
