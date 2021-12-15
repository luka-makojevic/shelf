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

const links = [
  {
    name: 'Shelves',
    path: Routes.DASHBOARD,
    icon: <FaList />,
    rolesWithAccess: [Role.USER, Role.ADMIN, Role.MASTER_ADMIN],
    childrenLinks: [
      { name: 'Shared', icon: <FaShare />, path: Routes.SHARED_SHELVES },
      { name: 'Trash', icon: <FaTrash />, path: Routes.TRASH },
    ],
  },
  {
    name: 'Functions',
    path: Routes.FUNCTIONS,
    icon: <AiOutlineFunction />,
    rolesWithAccess: [Role.USER, Role.ADMIN, Role.MASTER_ADMIN],
  },
  {
    name: 'Statistics',
    path: Routes.STATISTICS,
    icon: <FaChartLine />,
    rolesWithAccess: [Role.ADMIN, Role.MASTER_ADMIN],
  },
  {
    name: 'Users',
    path: Routes.USERS,
    icon: <FaUserFriends />,
    rolesWithAccess: [Role.MASTER_ADMIN],
  },
];

const NavigationSidebar = () => {
  const user = useAppSelector((state: RootState) => state.user.user);

  const [isOpen, setIsOpen] = useState(true);

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
            {links.map((link) => {
              const userHasRequiredRole =
                user && link.rolesWithAccess.includes(user?.role.id);

              if (user && !userHasRequiredRole) return null;

              if (link.childrenLinks) {
                return (
                  <NavigationSidebarItem
                    key={link.name}
                    label={link.name}
                    icon={link.icon}
                    to={link.path}
                  >
                    {link.childrenLinks.map((childLink) => (
                      <NavigationSidebarItem
                        key={childLink.name}
                        label={childLink.name}
                        icon={childLink.icon}
                        to={childLink.path}
                      />
                    ))}
                  </NavigationSidebarItem>
                );
              }

              return (
                <NavigationSidebarItem
                  key={link.name}
                  label={link.name}
                  icon={link.icon}
                  to={link.path}
                />
              );
            })}
          </NavigationSidebarContentWrapper>
        )}
      </NavigationSidebarInnerWrapper>
    </NavigationSidebarOuterWrapper>
  );
};

export default NavigationSidebar;
