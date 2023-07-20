import { Meta, StoryObj } from '@storybook/react';
import Textarea from '.';

const meta = {
  title: 'Components/common/Textarea',
  component: Textarea,
  tags: ['autodocs'],
} as Meta<typeof Textarea>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    placeholder: 'placeholder',
  },
};

export const WithValue: Story = {
  args: {
    value: '인풋이 이미 입력되어 있는 경우',
  },
};




