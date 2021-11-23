import { Container, Logo, Profile, Link } from './header-styles';

const Header = ({ hideProfile, ...restProps }: any) => {
  return (
    <Container {...restProps}>
      <Link to="/">
        <Logo src="./assets/images/logo.png" />
      </Link>

      <Profile hideProfile={hideProfile}>PP</Profile>
    </Container>
  );
};

export default Header;
