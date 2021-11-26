import Card from '../../components/card';
import { Logo } from '../../components/header/header-styles';
import {
  Wrapper,
  Container,
  Holder,
} from '../../components/layout/layout.styles';
import {
  Title,
  SubTitle,
  AccentText,
  Link,
} from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';

const featuresInfo = [
  {
    id: 0,
    img: './assets/images/cloud.png',
    text: 'Store and access your data from cloud',
  },
  {
    id: 1,
    img: './assets/images/share.png',
    text: 'Share your files and have access to files that are shared with you',
  },
  {
    id: 2,
    img: './assets/images/shelf.png',
    text: 'Built to store and retrive any amount of data from anywhere',
  },
  {
    id: 3,
    img: './assets/images/f.png',
    text: 'Event-driven compute service that lets you run code for virtually any type of application or backend service without provisioning or managing servers',
  },
];

const Landing = () => (
  <Wrapper flexDirection="column">
    <Container
      width="100%"
      height="100px"
      minHeight="100px"
      justifyContent="flex-end"
      px="50px"
    >
      <Link padding="10px 15px" mr="20px" to={Routes.LOGIN}>
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
    <Holder display="flex" flexDirection={['column', 'column', 'row']}>
      <Container
        flexDirection="column"
        width={['100%', '100%', '50%']}
        marginBottom="50px"
        padding={['20px', '80px']}
      >
        <Logo width="200px" src="./assets/images/logo.png" />
        <Title textAlign="center" fontSize="50px">
          Welcome to Shelf Storage Service
        </Title>
        <SubTitle textAlign="center" marginBottom="150px">
          Event-driven compute service that lets you run code for virtually any
          type of application or backend service without provisioning or
          managing servers
        </SubTitle>
        <AccentText>
          Start working more efficiently today,
          <Link to={Routes.REGISTER}> Sign up to get started</Link>
        </AccentText>
      </Container>
      <Container
        width={['100%', '100%', '50%']}
        display="grid"
        gridTemplateColumns={['1fr', '1fr 1fr']}
        gridGap="20px"
        padding={['20px', '80px']}
      >
        {featuresInfo.map((feature) => (
          <Card key={feature.id} image={feature.img} text={feature.text} />
        ))}
      </Container>
    </Holder>
  </Wrapper>
);

export default Landing;
