const fontWeight = {
  bold: 600,
  normal: 400,
} as const;

export type fontWeightThemeKey = keyof typeof fontWeight;

export default fontWeight;