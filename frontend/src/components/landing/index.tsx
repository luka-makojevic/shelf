import {
  Container,
  Content,
  Left,
  Right,
  Top,
  Logo,
  Title,
  Text,
  CallToAction,
  NavLink,
} from './landing-styles';

const Landing = ({ children, ...restProps }: any) => {
  return <Container {...restProps}>{children}</Container>;
};

Landing.Top = function LandingTop({ children, ...restProps }: any) {
  return <Top {...restProps}>{children}</Top>;
};

Landing.NavLink = function LandingNavLink({ children, ...restProps }: any) {
  return <NavLink {...restProps}>{children}</NavLink>;
};

Landing.Content = function LandingContent({ children, ...restProps }: any) {
  return <Content {...restProps}>{children}</Content>;
};

Landing.Logo = function LandingContent({ ...restProps }: any) {
  return (
    <Logo src="./assets/images/logo.png" alt="Shelf Logo" {...restProps} />
  );
};

Landing.Title = function LandingTitle({ children, ...restProps }: any) {
  return <Title {...restProps}>{children}</Title>;
};

Landing.Text = function LandingText({ children, ...restProps }: any) {
  return <Text {...restProps}>{children}</Text>;
};

Landing.CallToAction = function LandingCallToAction({
  children,
  ...restProps
}: any) {
  return <CallToAction {...restProps}>{children}</CallToAction>;
};

Landing.Left = function LandingLeft({ children, ...restProps }: any) {
  return <Left {...restProps}>{children}</Left>;
};

Landing.Right = function LandingRight({ children, ...restProps }: any) {
  return <Right {...restProps}>{children}</Right>;
};

export default Landing;
