import { ComponentMeta, ComponentStory } from '@storybook/react';
import CheckBox from '../components/checkbox/checkBox';

export default {
  title: 'UI/CheckBox',
  component: CheckBox,
} as ComponentMeta<typeof CheckBox>;

const Template: ComponentStory<typeof CheckBox> = (args) => (
  <CheckBox {...args} />
);

export const Primary = Template.bind({});
Primary.args = {
  children: 'checkbox',
};
