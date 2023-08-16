import { StoryObj, Meta } from '@storybook/react';
import TopicCard from '.';

const meta = {
  title: 'Components/TopicCard',
  component: TopicCard,
  tags: ['components'],
} as Meta<typeof TopicCard>;

export default meta;
type Story = StoryObj<typeof TopicCard>;

export const TopicCardComponent: Story = {
  args: {
    id: 1,
    image: '🍣',
    name: '선릉 직장인이 가는 맛집',
    creator: '패트릭',
    updatedAt: '2021-08-01',
    pinCount: 10,
    bookmarkCount: 5,
    isInAtlas: false,
    isBookmarked: false,
  },
};
