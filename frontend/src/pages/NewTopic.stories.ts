import { StoryObj, Meta } from "@storybook/react";
import NewTopic from "./NewTopic";

const meta = {
  title: "Pages/NewTopic",
  component: NewTopic,
  tags: ["pages"],
} as Meta<typeof NewTopic>;

export default meta;
type Story = StoryObj<typeof NewTopic>;

export const NewTopicPage: Story = {
  args: {},
};
