export const convertCSS = (property: number | string) => {
  if (typeof property === 'number') return `${property}px`;

  return property;
};
