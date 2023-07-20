const color = {
  primary: '#E1325C',
  black: '#454545',
  white: '#FFFFFF',

  darkGray: '#767676',
  gray: '#969696',
  lightGray: '#E7E7E7',
  whiteGray: '#F9F9F9'
} as const

export type colorThemeKey = keyof typeof color;

export default color
