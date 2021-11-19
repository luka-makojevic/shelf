import Card from '../../components/card'
import { Logo } from '../../components/header/header-styles'
import {
  Wrapper,
  Container,
  Feature,
  Holder,
} from '../../components/layout/layout.styles'
import {
  Title,
  SubTitle,
  AccentText,
  Link,
} from '../../components/text/text-styles'
import { Routes } from '../../enums/routes'

const featuresInfo = [
  {
    img: './assets/images/cloud.png',
    text: 'Store and access your data from cloud',
  },
  {
    img: './assets/images/share.png',
    text: 'Share your files and have access to files that are shared with you',
  },
  {
    img: './assets/images/shelf.png',
    text: 'Built to store and retrive any amount of data from anywhere',
  },
  {
    img: './assets/images/f.png',
    text: 'Event-driven compute service that lets you run code for virtually any type of application or backend service without provisioning or managing servers',
  },
]
<<<<<<< HEAD
// to do
const Landing = () => {
  return (
    <Wrapper flexDirection="column">
      <Container
        width="100%"
        height="100px"
        justifyContent="flex-end"
        px="50px"
      >
        <Link padding="30px" to={Routes.LOGIN}>
          Sign in
        </Link>
        <Link
          bg="primary"
          color="white"
          padding="10px 15px"
          borderRadius="10px"
          to={Routes.REGISTER}
        >
          Sign up
        </Link>
      </Container>
      <Holder height="100%">
        <Container
          flexDirection="column"
          width="50%"
          padding="50px 80px"
          marginBottom="50px"
        >
          <Logo width="200px" src="./assets/images/logo.png" />
          <Title textAlign="center" fontSize="50px">
            Welcome to Shelf Storage Service
          </Title>
          <SubTitle textAlign="center" marginBottom="150px">
=======

const Landing = () => {
  return (
    <LandingComponent>
      <LandingComponent.Top>
        <LandingComponent.NavLink to="/login">Sign in</LandingComponent.NavLink>
        <LandingComponent.NavLink to="/register">
          Sign up
        </LandingComponent.NavLink>
      </LandingComponent.Top>
      <LandingComponent.Content>
        <LandingComponent.Left>
          <LandingComponent.Logo />
          <Title>Welcome to Shelf Storage Service</Title>
          <SubTitle>
>>>>>>> master
            Event-driven compute service that lets you run code for virtually
            any type of application or backend service without provisioning or
            managing servers
          </SubTitle>
<<<<<<< HEAD
          <AccentText>
            Start working more efficiently today,
            <Link to={Routes.REGISTER}> Sign up to get started</Link>
          </AccentText>
        </Container>
      </Holder>
    </Wrapper>
=======
          <LandingComponent.CallToAction>
            Start working more efficiently today,{' '}
            <Link to="/register">Sign up to get started</Link>
          </LandingComponent.CallToAction>
        </LandingComponent.Left>
        <LandingComponent.Right>
          {featuresInfo.map((feature) => (
            <Card image={feature.img} text={feature.text} />
          ))}
        </LandingComponent.Right>
      </LandingComponent.Content>
    </LandingComponent>
>>>>>>> master
  )
}

export { Landing }
