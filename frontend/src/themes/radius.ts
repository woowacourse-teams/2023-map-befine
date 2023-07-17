const radius = {
  small: '4px',
  medium: '8px',
} as const

export type radiusKey = keyof typeof radius;

export default radius
