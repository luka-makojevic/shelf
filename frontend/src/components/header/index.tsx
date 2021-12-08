import { HeaderContainer, Logo, ProfileContainer } from './header-styles';
import { Routes } from '../../utils/enums/routes';
import { Link } from '../text/text-styles';
import { HeaderProps } from '../../utils/interfaces/propTypes';
import Profile from './profile';
import { Button } from '../UI/button';

const Header = ({ hideProfile, position, showButtons }: HeaderProps) => (
  <HeaderContainer position={position}>
    <Link to={Routes.HOME}>
      <Logo src={`${process.env.PUBLIC_URL}/assets/images/logo.png`} />
    </Link>
    {showButtons && (
      <div>
        <Button variant="light" to={Routes.LOGIN}>
          Sign in
        </Button>
        <Button to={Routes.REGISTER}>Sign up</Button>
      </div>
    )}
    {!hideProfile && (
      <ProfileContainer hideProfile={hideProfile}>
        <Profile />
      </ProfileContainer>
    )}
  </HeaderContainer>
);

export default Header;
