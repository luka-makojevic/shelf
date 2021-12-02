import { useContext, useState } from 'react';
import {
  Container,
  Logo,
  Profile,
  DropDown,
  ProfilePicture,
} from './header-styles';
import { AuthContext } from '../../providers/authProvider';
import { Button } from '../UI/button';
import { Routes } from '../../enums/routes';
import { Holder } from '../layout/layout.styles';
import { PlainText, Link } from '../text/text-styles';

type HeaderProps = {
  hideProfile: boolean;
};

const Header = ({ hideProfile }: HeaderProps) => {
  const [showDropdown, setShowDropdown] = useState<boolean>(false);

  const { logout, user } = useContext(AuthContext);
  const data = {
    jwtRefreshToken: user?.jwtRefreshToken,
    jwtToken: user?.jwtToken,
  };
  const handleDropdownVisibility = () => {
    setShowDropdown(!showDropdown);
  };
  const handleLogout = () => {
    logout(
      data,
      () => {},
      () => {}
    );
  };

  return (
    <Container>
      <Link to={Routes.HOME}>
        <Logo src={`${process.env.PUBLIC_URL}/assets/images/logo.png`} />
      </Link>
      <Profile hideProfile={hideProfile}>
        <ProfilePicture onClick={handleDropdownVisibility} />
        {showDropdown && (
          <DropDown>
            <ProfilePicture />
            <Holder ml="10px">
              <PlainText>
                {user?.firstName} {user?.lastName}
              </PlainText>
              <PlainText>{user?.email}</PlainText>
              <Link to="/manage-account">manage account</Link>
              <Holder display="flex" justifyContent="flex-end">
                <Button onClick={handleLogout} size="small" m="8px 0 0 0">
                  log out
                </Button>
              </Holder>
            </Holder>
          </DropDown>
        )}
      </Profile>
    </Container>
  );
};

export default Header;
