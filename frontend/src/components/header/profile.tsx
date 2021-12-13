import { useState } from 'react';
import { useAuth } from '../../hooks/authHook';
import { useAppSelector } from '../../store/hooks';
import { PlainText, Link } from '../text/text-styles';
import { Button } from '../UI/button';
import { DropDown, ProfilePicture, ProfileInfoWrapper } from './header-styles';

const Profile = () => {
  const [showDropdown, setShowDropdown] = useState<boolean>(false);

  const { logout } = useAuth();

  const user = useAppSelector((state) => state.user.user);

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
    <>
      <ProfilePicture onClick={handleDropdownVisibility} />
      {showDropdown && (
        <DropDown>
          <div>
            <ProfilePicture />
          </div>
          <ProfileInfoWrapper>
            <PlainText>
              {user?.firstName} {user?.lastName}
            </PlainText>
            <PlainText>{user?.email}</PlainText>
            <Link to="/manage-account">manage account</Link>
            <div>
              <Button onClick={handleLogout} size="small">
                log out
              </Button>
            </div>
          </ProfileInfoWrapper>
        </DropDown>
      )}
    </>
  );
};

export default Profile;
