import { Routes } from '../../enums/routes';
import { Container, Logo, Profile, Link } from './header-styles';

type HeaderProps = {
  hideProfile: boolean;
};

const Header = ({ hideProfile }: HeaderProps) => (
  <Container>
    <Link to={Routes.HOME}>
      <Logo width="70px" src="./assets/images/logo.png" />
    </Link>
    <Profile hideProfile={hideProfile}>PP</Profile>
  </Container>
);

export default Header;
