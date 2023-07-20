import { Meta, StoryObj } from "@storybook/react";
import Button from ".";

const meta = {
  title: "Components/common/Button",
  component: Button,
  tags: ["autodocs"],
  argTypes: {
    variant: {
      control: {
        type: "radio",
      },
      options: ["primary", "secondary"],
      defaultValue: "primary",
      table: {
        defaultValue: { summary: "primary" },
        type: { summary: "primary | secondary" },
      },
    },
    children: {
      control: {
        type: "text",
      },
      defaultValue: "Button",
      table: {
        defaultValue: { summary: "Button" },
        type: { summary: "string" },
      },
    },
    disabled: {
      control: {
        type: "boolean",
      },
      defaultValue: false,
      description: "버튼 비활성화 여부",
      table: {
        defaultValue: { summary: false },
        type: { summary: "boolean" },
      },
    },
    onClick: {
      description: "버튼 클릭 이벤트",
      table: {
        type: { summary: "function" },
      },
    },
  },
} satisfies Meta<typeof Button>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    variant: "primary",
    children: "primary button",
  },
};

export const Secondary: Story = {
  args: {
    variant: "secondary",
    children: "secondary button",
  },
};

export const Disabled: Story = {
  args: {
    variant: "primary",
    children: "disabled button",
    disabled: true,
  },
};
