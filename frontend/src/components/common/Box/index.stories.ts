import { StoryObj, Meta } from "@storybook/react";
import Box from ".";

const meta = {
  title: "Components/common/Box",
  component: Box,
  tags: ["autodocs"],
  argTypes: {
    display: {
      control: {
        type: "select",
      },
      options: ["flex", "block", "inline-block"],
      defaultValue: "flex",
      table: {
        defaultValue: { summary: "flex" },
        type: { summary: "flex | block | inline-block" },
      },
    },
    width: {
      control: {
        type: "text",
      },
      defaultValue: "100%",
      table: {
        defaultValue: { summary: "100%" },
        type: { summary: "string" },
      },
    },
    height: {
      control: {
        type: "text",
      },
      defaultValue: "100%",
      table: {
        defaultValue: { summary: "100%" },
        type: { summary: "string" },
      },
    },
    padding: {
      control: {
        type: "select",
        options: [0, 1, 2, 3, 4, 5, 6],
      },
      defaultValue: 0,
      table: {
        defaultValue: { summary: 0 },
        type: { summary: "number" },
      },
    },
    $backgroundColor: {
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
    border: {
      control: {
        type: "select",
      },
      options: ["none", "1px solid black"],
      defaultValue: "none",
      table: {
        defaultValue: { summary: "none" },
        type: { summary: "none | 1px solid black" },
      },
    },
    overflow: {
      control: {
        type: "select",
      },
      options: ["visible", "hidden", "scroll", "auto"],
      defaultValue: "visible",
      table: {
        defaultValue: { summary: "visible" },
        type: { summary: "visible | hidden | scroll | auto" },
      },
    },
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
    $borderRadius: {
      control: {
        type: "radio",
      },
      options: ["small", "medium"],
      defaultValue: "small",
      table: {
        defaultValue: { summary: "small" },
        type: { summary: "small | medium" },
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
  },

} as Meta<typeof Box>;

export default meta;
type Story = StoryObj<typeof meta>;

//make a Default has background Color


export const Default: Story = {
  args: {
    children: "Box",
  },
};
