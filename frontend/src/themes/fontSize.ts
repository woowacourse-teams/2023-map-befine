const fontSize = {
  extraLarge: '24px',
  large: '20px',
  medium: '18px',
  default: '16px',
  small: '14px',
  extraSmall: '12px',
} as const

export type fontSizeThemeKey = keyof typeof fontSize;


export default fontSize
