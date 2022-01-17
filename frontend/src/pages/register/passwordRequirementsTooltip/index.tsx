import {
  ListItem,
  List,
  Text,
  TooltipWrapper,
} from './passwordRequirementsTooltip.styles';

const PasswordRequirementsTooltip = () => (
  <TooltipWrapper>
    <Text>
      Passwords must contain at least 8 characters and contain the following:
    </Text>
    <List>
      <ListItem>Uppercase letter</ListItem>
      <ListItem>Lowercase letter</ListItem>
      <ListItem>Number</ListItem>
      <ListItem>Special character</ListItem>
    </List>
  </TooltipWrapper>
);

export default PasswordRequirementsTooltip;
