import { Link } from 'react-router-dom';
import { Landing as LandingComponent } from '../../components';
import Card from '../../components/card';
import { Title, SubTitle } from '../../components/text/text-styles';

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
            Event-driven compute service that lets you run code for virtually
            any type of application or backend service without provisioning or
            managing servers
          </SubTitle>
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
  );
};

export { Landing };
