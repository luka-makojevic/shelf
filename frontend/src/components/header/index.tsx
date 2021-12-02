import { Container, Logo, ProfileContainer } from './header-styles';
import { Routes } from '../../enums/routes';
import { Link } from '../text/text-styles';
import { HeaderProps } from '../../interfaces/types';
import Profile from './profile';

const Header = ({ hideProfile }: HeaderProps) => {
  return (
    <Container>
      <Link to={Routes.HOME}>
        <Logo src={`${process.env.PUBLIC_URL}/assets/images/logo.png`} />
      </Link>
      <ProfileContainer hideProfile={hideProfile}>
        <Profile />
      </ProfileContainer>
    </Container>
  );
};

export default Header;
