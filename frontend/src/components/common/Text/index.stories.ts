import { Meta, StoryObj } from "@storybook/react";
import Text from ".";

const meta = {
  title: "Components/common/Text",
  component: Text,
  tags: ["autodocs"],
  argTypes: {
    color: {
      control: {
        type: "radio",
      },
      options: ["primary", "black", "white", "darkGray", "gray", "lightGray", "whiteGray"],
      defaultValue: "primary",
      table: {
        defaultValue: { summary: "primary" },
        type: { summary: "primary | black | white | darkGray |gray | lightGray | whiteGray" },
      },
    },
    $fontSize: {
      control: {
        type: "radio",
      },
      options: ["extraSmall", "small", "default", "medium", "large", "extraLarge"],
      defaultValue: "default",
      table: {
        defaultValue: { summary: "default" },
        type: { summary: "extraSmall | small | default | medium | large | extraLarge" },
      },
    },
    $fontWeight: {
      control: {
        type: "radio",
      },
      options: ["normal", "bold"],
      defaultValue: "normal",
      description: "폰트 굵기",
      table: {
        defaultValue: { summary: "normal" },
        type: { summary: "normal | bold" },
      },
    },
    children: {
      control: {
        type: "text",
      },
      defaultValue: "Text",
      table: {
        defaultValue: { summary: "Text" },

        type: { summary: "string" },
      },
    },
  },
} satisfies Meta<typeof Text>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    color: "black",
    $fontSize: "default",
    $fontWeight: "normal",
    children: "Text",
  },
};

export const Bold: Story = {
  args: {
    color: "black",
    $fontSize: "large",
    $fontWeight: "bold",
    children: "Text",
  },
};

