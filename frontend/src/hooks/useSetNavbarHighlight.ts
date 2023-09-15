import { useContext, useEffect } from 'react';
import {
  NavbarHighlightKeys,
  NavbarHighlights,
  NavbarHighlightsContext,
} from '../context/NavbarHighlightsContext';

const navbarPageNames: NavbarHighlightKeys[] = [
  'home',
  'seeTogether',
  'addMapOrPin',
  'favorite',
  'profile',
];

const useSetNavbarHighlight = (pageName: NavbarHighlightKeys) => {
  const { navbarHighlights, setNavbarHighlights } = useContext(
    NavbarHighlightsContext,
  );

  useEffect(() => {
    const newNavbarHighlights: NavbarHighlights = navbarPageNames.reduce(
      (acc, curr) => ({ ...acc, [curr]: curr === pageName }),
      {} as NavbarHighlights,
    );

    setNavbarHighlights(newNavbarHighlights);
  }, [pageName]);

  return { navbarHighlights };
};

export default useSetNavbarHighlight;
