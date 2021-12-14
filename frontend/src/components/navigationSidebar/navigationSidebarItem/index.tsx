import { useState } from 'react';
import { FaCaretDown, FaCaretUp } from 'react-icons/fa';
import {
  Item,
  ItemChildrenWrapper,
  ItemIconButton,
  ItemWrapper,
  NavigationSidebarLink,
} from './navigationSidebarItem-styles';
import { NavigationSidebarItemProps } from './navigationSidebarItem.interfaces';

const NavigationSidebarItem = ({
  children,
  label,
  icon,
  to,
}: NavigationSidebarItemProps) => {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const handleCollapse = () => {
    setIsCollapsed(!isCollapsed);
  };

  const item = (
    <Item>
      {icon}
      <NavigationSidebarLink to={to}>{label}</NavigationSidebarLink>
    </Item>
  );

  if (children) {
    return (
      <>
        <ItemWrapper>
          {item}
          <ItemIconButton onClick={handleCollapse}>
            {isCollapsed ? <FaCaretDown /> : <FaCaretUp />}
          </ItemIconButton>
        </ItemWrapper>
        <ItemChildrenWrapper>{!isCollapsed && children}</ItemChildrenWrapper>
      </>
    );
  }

  return <ItemWrapper>{item}</ItemWrapper>;
};

export default NavigationSidebarItem;
