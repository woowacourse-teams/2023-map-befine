import color from "./color"
import fontSize from "./fontSize"
import spacing from "./spacing"
import radius from "./radius"

const theme = {
  fontSize,
  color,
  spacing,
  radius,
} as const

export type AppTheme = typeof theme;

export default theme
