import { StoryObj, Meta } from '@storybook/react';
import Home from './Home'


const meta = {
  title: 'Pages/Home',
  component: Home,
  tags: ['pages'],
} as Meta<typeof Home>;

export default meta;
type Story = StoryObj<typeof Home>;

export const HomePage: Story = {
  args: {}
}
