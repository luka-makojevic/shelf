import { ComponentStory, ComponentMeta } from '@storybook/react';
import { Dispatch, SetStateAction } from 'react';
import SearchBar from './searchBar';

export default {
  title: 'UI/SearchBar',
  component: SearchBar,
} as ComponentMeta<typeof SearchBar>;

const Template: ComponentStory<typeof SearchBar> = (args) => (
  <SearchBar {...args} />
);

// eslint-disable-next-line @typescript-eslint/no-explicit-any
const setData: Dispatch<SetStateAction<any>> = () => {};

export const Primary = Template.bind({});
Primary.args = {
  placeholder: 'Search...',
  searchKey: 'name',
  data: [],
  setData,
};

export const Secondary = Template.bind({});
Secondary.args = {
  searchKey: 'name',
  data: [],
  setData,
};
