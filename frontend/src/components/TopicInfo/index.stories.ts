import { StoryObj, Meta } from "@storybook/react";
import TopicInfo from ".";

const meta = {
  title: "Components/TopicInfo",
  component: TopicInfo,
  tags: ["components"],
} as Meta<typeof TopicInfo>;

export default meta;
type Story = StoryObj<typeof TopicInfo>;

export const TopicInfoComponent: Story = {
  args: {
    topicParticipant: 3,
    pinNumber: 5,
    topicTitle: "선릉 직장인이 가는 맛집",
    topicOwner: "오또상",
    topicDescription: "선릉 직장인이 돌아다니면서 기록한 맛집 리스트예요."
  }
};

