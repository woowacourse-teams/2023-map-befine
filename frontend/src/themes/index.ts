import color from './color';
import fontSize from './fontSize';
import fontWeight from './fontWeight';
import radius from './radius';
import spacing from './spacing';

const theme = {
  fontSize,
  fontWeight,
  color,
  spacing,
  radius,
} as const;

export type AppTheme = typeof theme;

export default theme;
