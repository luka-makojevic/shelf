import Card from '../../components/card';
import {
  Wrapper,
  GridContainer,
  LandinPageContainer,
} from '../../components/layout/layout.styles';
import {
  H1,
  Description,
  AccentText,
  Link,
} from '../../components/text/text-styles';
import { Routes } from '../../utils/enums/routes';
import { Header } from '../../components';

const featuresInfo = [
  {
    id: 0,
    img: './assets/images/cloud.png',
    text: 'Store and access your data from cloud',
    alt: 'cloud upload',
  },
  {
    id: 1,
    img: './assets/images/share.png',
    text: 'Share your files and have access to files that are shared with you',
    alt: 'share',
  },
  {
    id: 2,
    img: './assets/images/shelf.png',
    text: 'Built to store and retrive any amount of data from anywhere',
    alt: 'shelf',
  },
  {
    id: 3,
    img: './assets/images/f.png',
    text: 'Event-driven compute service that lets you run code for virtually any type of application or backend service without provisioning or managing servers',
    alt: 'lambda function',
  },
];

const Landing = () => (
  <>
    <Header showButtons hideProfile position="absolute" />
    <Wrapper>
      <LandinPageContainer>
        <H1>Welcome to Shelf Storage Service</H1>
        <Description>
          Event-driven compute service that lets you run code for virtually any
          type of application or backend service without provisioning or
          managing servers
        </Description>
        <AccentText>
          Start working more efficiently today,
          <Link to={Routes.REGISTER}>Sign up to get started</Link>
        </AccentText>
      </LandinPageContainer>
      <LandinPageContainer>
        <GridContainer>
          {featuresInfo.map((feature) => (
            <Card
              key={feature.id}
              alt={feature.alt}
              image={feature.img}
              text={feature.text}
            />
          ))}
        </GridContainer>
      </LandinPageContainer>
    </Wrapper>
  </>
);

export default Landing;
