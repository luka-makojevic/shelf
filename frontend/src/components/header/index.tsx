import { Routes } from '../../enums/routes';
import { Container, Logo, Profile, Link, DropDown } from './header-styles';
import authServices from '../../services/authServices';
import { useContext } from 'react';
import { AuthContext } from '../../providers/authProvider';
import { Button } from '../UI/button';

type HeaderProps = {
  hideProfile: boolean;
};

const Header = ({ hideProfile }: HeaderProps) => {
  const { logout, user } = useContext(AuthContext);
  const data = {
    jwtRefreshToken: user?.jwtRefreshToken,
    jwtToken: user?.jwtToken,
  };

  return (
    <Container>
      <Link to={Routes.HOME}>
        <Logo src="./assets/images/logo.png" />
      </Link>
      <Profile hideProfile={hideProfile}>
        PP
        <DropDown>
          <p>PP</p>
          <div>
            <Button
              onClick={() => {
                logout(data);
              }}
              size="small"
              variant="light"
            >
              log out
            </Button>
          </div>
        </DropDown>
      </Profile>
    </Container>
  );
};

export default Header;
