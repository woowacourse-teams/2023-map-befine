declare module '*.svg' {
  import React from 'react';
  const SVG: React.VFC<React.SVGProps<SVGSVGElement>>;
  export default SVG;
}

declare global {
  interface Window {
    Tmapv2: Tmapv2;
  }
}
