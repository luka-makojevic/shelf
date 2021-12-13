import { useState } from 'react';
import {
  FaAngleDoubleLeft,
  FaAngleDoubleRight,
  FaChartLine,
  FaList,
  FaShare,
  FaTrash,
  FaUserFriends,
} from 'react-icons/fa';
import { AiOutlineFunction } from 'react-icons/ai';
import { Role } from '../../utils/enums/roles';
import {
  NavigationSidebarToggleButton,
  NavigationSidebarOuterWrapper,
  NavigationSidebarInnerWrapper,
  NavigationSidebarContentWrapper,
} from './navigationSidebar-styles';
import NavigationSidebarItem from './navigationSidebarItem';
import { useAppSelector } from '../../store/hooks';
import { RootState } from '../../store/store';
import { Routes } from '../../utils/enums/routes';

const NavigationSidebar = () => {
  const user = useAppSelector((state: RootState) => state.user.user);
  const [isOpen, setIsOpen] = useState(true);

  const canAccessStatisticsPage =
    user?.role === Role.ADMIN || user?.role === Role.MASTER_ADMIN;
  const canAccessUserManagementPage = user?.role === Role.MASTER_ADMIN;

  const handleNavigationSidebarToggle = () => {
    setIsOpen(!isOpen);
  };

  return (
    <NavigationSidebarOuterWrapper>
      <NavigationSidebarInnerWrapper>
        <NavigationSidebarToggleButton onClick={handleNavigationSidebarToggle}>
          {isOpen ? <FaAngleDoubleLeft /> : <FaAngleDoubleRight />}
        </NavigationSidebarToggleButton>
        {isOpen && (
          <NavigationSidebarContentWrapper>
            <NavigationSidebarItem
              label="Shelves"
              icon={<FaList />}
              to={Routes.DASHBOARD}
            >
              <NavigationSidebarItem
                label="Shared"
                icon={<FaShare />}
                to={Routes.SHARED_SHELVES}
              />
              <NavigationSidebarItem
                label="Trash"
                icon={<FaTrash />}
                to={Routes.TRASH}
              />
            </NavigationSidebarItem>
            <NavigationSidebarItem
              label="Functions"
              icon={<AiOutlineFunction />}
              to={Routes.FUNCTIONS}
            />
            {canAccessStatisticsPage && (
              <NavigationSidebarItem
                label="Statistics"
                icon={<FaChartLine />}
                to={Routes.STATISTICS}
              />
            )}
            {canAccessUserManagementPage && (
              <NavigationSidebarItem
                label="Users"
                icon={<FaUserFriends />}
                to={Routes.USERS}
              />
            )}
          </NavigationSidebarContentWrapper>
        )}
      </NavigationSidebarInnerWrapper>
    </NavigationSidebarOuterWrapper>
  );
};

export default NavigationSidebar;
