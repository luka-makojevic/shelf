import { useEffect, useRef, useState } from 'react';
import { useAuth } from '../../hooks/authHook';
import fileServices from '../../services/fileServices';
import { useAppSelector } from '../../store/hooks';
import { PlainText, Link } from '../text/text-styles';
import { Button } from '../UI/button';
import { DropDown, ProfilePicture, ProfileInfoWrapper } from './header-styles';

const Profile = () => {
  const [showDropdown, setShowDropdown] = useState<boolean>(false);

  const { logout } = useAuth();

  const user = useAppSelector((state) => state.user.user);

  const data = {
    jwtRefreshToken: JSON.parse(localStorage.getItem('refreshToken') || ''),
    jwtToken: JSON.parse(localStorage.getItem('token') || ''),
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
  const dropDownRef = useRef<HTMLDivElement>(null);
  const profileRef = useRef<HTMLDivElement>(null);
  useEffect(() => {
    const handleOutsideClick = (event: MouseEvent) => {
      if (
        dropDownRef.current &&
        profileRef.current &&
        event.target instanceof Node &&
        !dropDownRef.current.contains(event.target) &&
        !profileRef.current.contains(event.target)
      ) {
        setShowDropdown(false);
      }
    };

    document.addEventListener('mousedown', handleOutsideClick);
    return () => {
      document.removeEventListener('mousedown', handleOutsideClick);
    };
  }, [dropDownRef, profileRef]);

  const imgUrl = fileServices.getProfilePicture(user?.id);

  return (
    <>
      <ProfilePicture
        imgUrl={imgUrl}
        ref={profileRef}
        onClick={handleDropdownVisibility}
      />
      {showDropdown && (
        <DropDown ref={dropDownRef}>
          <div>
            <ProfilePicture imgUrl={imgUrl} />
          </div>
          <ProfileInfoWrapper>
            <PlainText>
              {user?.firstName} {user?.lastName}
            </PlainText>
            <PlainText>{user?.email}</PlainText>
            {user?.role.id !== 1 && (
              <Link to={`/profile/${user?.id}`}>manage account</Link>
            )}
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
