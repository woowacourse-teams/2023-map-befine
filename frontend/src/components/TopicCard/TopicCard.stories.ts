import { StoryObj, Meta } from "@storybook/react";
import TopicCard from ".";

const meta = {
  title: "Components/TopicCard",
  component: TopicCard,
  tags: ["components"],
} as Meta<typeof TopicCard>;

export default meta;
type Story = StoryObj<typeof TopicCard>;

export const TopicCardComponent: Story = {
  args: {
    topicId: "1",
    topicEmoji: "🍣",
    topicTitle: "선릉 직장인이 가는 맛집",
    topicUpdatedAt: "2021-08-01",
    topicPinCount: 10,
  },
};




