import { StoryObj, Meta } from "@storybook/react";
import PinDetail from "./PinDetail";

const meta = {
  title: "Pages/PinDetail",
  component: PinDetail,
  tags: ["pages"],
} as Meta<typeof PinDetail>;

export default meta;
type Story = StoryObj<typeof PinDetail>;

export const PinDetailPage: Story = {
  args: {
    pinId: "1",
  },
};
