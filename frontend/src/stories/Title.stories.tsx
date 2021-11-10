import React from 'react'
import { ComponentStory, ComponentMeta } from '@storybook/react'

import { Title } from '../components/feature/feature-styles'

// More on default export: https://storybook.js.org/docs/react/writing-stories/introduction#default-export
export default {
  title: 'Example/Title',
  component: Title,
  // More on argTypes: https://storybook.js.org/docs/react/api/argtypes
  argTypes: {
    backgroundColor: { control: 'color' },
  },
} as ComponentMeta<typeof Title>

// More on component templates: https://storybook.js.org/docs/react/writing-stories/introduction#using-args
const Template: ComponentStory<typeof Title> = (args) => <Title {...args} />

export const Heading1 = Template.bind({})
// More on args: https://storybook.js.org/docs/react/writing-stories/args
Heading1.args = {
  fontSize: 5,
  children: 'Hello',
}

export const Heading2 = Template.bind({})
// More on args: https://storybook.js.org/docs/react/writing-stories/args
Heading2.args = {
  fontSize: 3,
  children: 'Heading 2',
}
