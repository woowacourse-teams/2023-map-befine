import { useEffect, useRef, useState } from 'react';

const useMediaQuery = (
  mediaQueries: number[] = [],
  defaultValueIgnoreMedia: number = 1,
) => {
  const currentPageWidth = window.innerWidth;

  const mediaQueriesIncludeInit = mediaQueries
    .concat(currentPageWidth)
    .sort((a, b) => b - a);

  const [matches, setMatches] = useState(
    mediaQueriesIncludeInit.length > 1
      ? mediaQueriesIncludeInit.length -
          mediaQueriesIncludeInit.indexOf(currentPageWidth)
      : defaultValueIgnoreMedia,
  );
  const mediaQueryListRef = useRef<string[] | null>(null);

  useEffect(() => {
    mediaQueryListRef.current = mediaQueriesIncludeInit.map(
      (mediaQuery) => `(max-width: ${mediaQuery}px)`,
    );

    const handleMediaChange = (elementsCount: number) => {
      setMatches(elementsCount);
    };

    mediaQueryListRef.current.forEach((mediaQuery, index) => {
      const matchMedia = window.matchMedia(mediaQuery);

      if (mediaQueryListRef.current) {
        const mediaQueryConditions = mediaQueryListRef.current;

        matchMedia.addEventListener('change', () =>
          handleMediaChange(mediaQueryConditions.length - index),
        );
      }
    });

    return () => {
      if (mediaQueryListRef.current) {
        const mediaQueryConditions = mediaQueryListRef.current;

        mediaQueryConditions.forEach((mediaQuery, index) => {
          const matchMedia = window.matchMedia(mediaQuery);

          matchMedia.removeEventListener('change', () =>
            handleMediaChange(mediaQueryConditions.length - index),
          );
        });
      }
    };
  }, []);

  return { elementsCount: matches };
};

export default useMediaQuery;
