import { ComponentStory, ComponentMeta } from '@storybook/react';
import { Button } from '.';

export default {
  title: 'Example/Button',
  component: Button,
  argTypes: {
    size: {
      options: ['primary', 'large', 'small'],
      control: { type: 'radio' },
    },
  },
} as ComponentMeta<typeof Button>;

const Template: ComponentStory<typeof Button> = (args) => <Button {...args} />;
export const Primary = Template.bind({});
Primary.args = {
  variant: 'primary',
  children: 'Button',
};
export const Secondary = Template.bind({});
Secondary.args = {
  variant: 'secondary',
  children: 'Button',
};
export const Spinner = Template.bind({});
Spinner.args = {
  variant: 'primary',
  children: 'Button',
  spinner: true,
  isLoading: true,
};
export const IconButton = Template.bind({});
IconButton.args = {
  variant: 'primary',
  children: 'Button',
  icon: (
    <img
      src={`${process.env.PUBLIC_URL}/assets/images/share.png`}
      alt="loading"
    />
  ),
};
