import { Meta, StoryObj } from "@storybook/react";
import Input from ".";

const meta = {
  title: "Components/common/Input",
  component: Input,
  tags: ["autodocs"],
} satisfies Meta<typeof Input>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    placeholder: "placeholder",
  },
};