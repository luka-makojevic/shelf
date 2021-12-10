import React from 'react';
import { ComponentStory, ComponentMeta } from '@storybook/react';
import { Button } from '.';
// More on default export: https://storybook.js.org/docs/react/writing-stories/introduction#default-export
export default {
  title: 'Example/Button',
  component: Button,
  argTypes: {
    size: {
      options: ['primary', 'large', 'small'],
      control: { type: 'radio' },
    },
  },
  // More on argTypes: https://storybook.js.org/docs/react/api/argtypes
} as ComponentMeta<typeof Button>;
// More on component templates: https://storybook.js.org/docs/react/writing-stories/introduction#using-args
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
