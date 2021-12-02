import React, { useContext, useState } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../../providers/authProvider';
import { Holder } from '../layout/layout.styles';
import { PlainText } from '../text/text-styles';
import { Button } from '../UI/button';
import { DropDown, ProfilePicture } from './header-styles';

const Profile = () => {
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
    <>
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
              <Button onClick={handleLogout} size="small">
                log out
              </Button>
            </Holder>
          </Holder>
        </DropDown>
      )}
    </>
  );
};

export default Profile;
