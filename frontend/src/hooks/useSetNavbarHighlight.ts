import { useContext, useEffect } from 'react';

import {
  NavbarHighlightKeys,
  NavbarHighlights,
  NavbarHighlightsContext,
} from '../context/NavbarHighlightsContext';

type NavbarPageNamesType = 'none' | NavbarHighlightKeys;

const navbarPageNames: NavbarPageNamesType[] = [
  'home',
  'seeTogether',
  'addMapOrPin',
  'favorite',
  'profile',
  'none',
];

const useSetNavbarHighlight = (pageName: NavbarPageNamesType) => {
  const { navbarHighlights, setNavbarHighlights } = useContext(
    NavbarHighlightsContext,
  );

  useEffect(() => {
    if (pageName === 'none') return;

    const newNavbarHighlights: NavbarHighlights = navbarPageNames.reduce(
      (acc, curr) => ({ ...acc, [curr]: curr === pageName }),
      {} as NavbarHighlights,
    );

    setNavbarHighlights(newNavbarHighlights);
  }, [pageName]);

  return { navbarHighlights };
};

export default useSetNavbarHighlight;
