import color from "./color"
import fontSize from "./fontSize"
import spacing from "./spacing"
import radius from "./radius"
import fontWeight from "./fontWeight"

const theme = {
  fontSize,
  fontWeight,
  color,
  spacing,
  radius,
} as const

export type AppTheme = typeof theme;

export default theme
