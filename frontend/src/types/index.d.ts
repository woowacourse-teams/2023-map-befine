declare module '*.svg' {
  import React from 'react';

  const SVG: React.VFC<React.SVGProps<SVGSVGElement>>;
  export default SVG;
}

declare module '*.webp' {
  import React from 'react';

  const WEBP: string;
  export default WEBP;
}
