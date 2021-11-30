import React from 'react';
import Card from '../../components/card';
import { Logo } from '../../components/header/header-styles';
import {
  Wrapper,
  Container,
  Feature,
  Holder,
} from '../../components/layout/layout.styles';
import {
  Title,
  SubTitle,
  AccentText,
  Link,
} from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';
import { Button } from '../../components/UI/button';

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
];
// to do
const Landing = () => (
  <Wrapper flexDirection="column">
    <Container width="100%" height="100px" justifyContent="flex-end" px="50px">
      <Button variant="light" to={Routes.LOGIN}>
        Sign in
      </Button>
      <Button to={Routes.REGISTER}>Sign up</Button>
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
          Event-driven compute service that lets you run code for virtually any
          type of application or backend service without provisioning or
          managing servers
        </SubTitle>
        <AccentText>
          Start working more efficiently today,
          <Link to={Routes.REGISTER}> Sign up to get started</Link>
        </AccentText>
      </Container>
    </Holder>
  </Wrapper>
);

export default Landing;
