import { useContext, useEffect } from 'react';
import { NavbarHighlightsContext } from '../context/NavbarHighlightsContext';

const navbarPageNames = [
  'home',
  'seeTogether',
  'addMapOrPin',
  'favorite',
  'profile',
];

const useSetNavbarHighlight = (pageName: string) => {
  const { navbarHighlights, setNavbarHighlights } = useContext(
    NavbarHighlightsContext,
  );

  useEffect(() => {
    if (!navbarPageNames.includes(pageName)) {
      setNavbarHighlights({
        home: false,
        seeTogether: false,
        addMapOrPin: false,
        favorite: false,
        profile: false,
      });

      return;
    }

    if (pageName === 'home') {
      setNavbarHighlights({
        home: true,
        seeTogether: false,
        addMapOrPin: false,
        favorite: false,
        profile: false,
      });

      return;
    }

    if (pageName === 'seeTogether') {
      setNavbarHighlights({
        home: false,
        seeTogether: true,
        addMapOrPin: false,
        favorite: false,
        profile: false,
      });

      return;
    }

    if (pageName === 'addMapOrPin') {
      setNavbarHighlights({
        home: false,
        seeTogether: false,
        addMapOrPin: true,
        favorite: false,
        profile: false,
      });

      return;
    }

    if (pageName === 'favorite') {
      setNavbarHighlights({
        home: false,
        seeTogether: false,
        addMapOrPin: false,
        favorite: true,
        profile: false,
      });

      return;
    }

    if (pageName === 'profile') {
      setNavbarHighlights({
        home: false,
        seeTogether: false,
        addMapOrPin: false,
        favorite: false,
        profile: true,
      });

      return;
    }
  }, []);

  return { navbarHighlights };
};

export default useSetNavbarHighlight;
