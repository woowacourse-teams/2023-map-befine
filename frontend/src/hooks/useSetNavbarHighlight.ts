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

const deleteNavbarHighlights = {
  home: false,
  seeTogether: false,
  addMapOrPin: false,
  favorite: false,
  profile: false,
};

const useSetNavbarHighlight = (pageName: NavbarPageNamesType) => {
  const { navbarHighlights, setNavbarHighlights } = useContext(
    NavbarHighlightsContext,
  );

  useEffect(() => {
    if (pageName === 'none') {
      setNavbarHighlights(deleteNavbarHighlights);
      return;
    }

    const newNavbarHighlights: NavbarHighlights = navbarPageNames.reduce(
      (acc, cur) => ({ ...acc, [cur]: cur === pageName }),
      {} as NavbarHighlights,
    );

    setNavbarHighlights(newNavbarHighlights);
  }, [pageName]);

  return { navbarHighlights };
};

export default useSetNavbarHighlight;
