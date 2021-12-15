import { ComponentStory, ComponentMeta } from '@storybook/react';
import { Table } from './table';

const data = [
  {
    name: 'picture shelf',
    creation_date: '11/8/1991',
    id: 2,
  },
  { name: 'documents', creation_date: '11/1/2001', id: 1 },
  { name: 'videos', creation_date: '11/4/2021', id: 3 },
];
const headers = [
  { header: 'Name', key: 'name' },
  { header: 'Creation date', key: 'creation_date' },
  { header: 'Id', key: 'id' },
];

export default {
  title: 'Table',
  component: Table,
} as ComponentMeta<typeof Table>;

const Template: ComponentStory<typeof Table> = (args) => (
  <Table {...args} data={data} headers={headers} />
);

export const Primary = Template.bind({});
