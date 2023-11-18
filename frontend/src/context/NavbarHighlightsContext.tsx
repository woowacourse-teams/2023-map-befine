import {
  createContext,
  Dispatch,
  ReactNode,
  SetStateAction,
  useState,
} from 'react';

export type NavbarHighlightKeys =
  | 'home'
  | 'seeTogether'
  | 'addMapOrPin'
  | 'favorite'
  | 'profile';

export type NavbarHighlights = {
  [key in NavbarHighlightKeys]: boolean;
};

interface NavbarHighlightsProviderProps {
  children: ReactNode;
}

export const NavbarHighlightsContext = createContext<{
  navbarHighlights: NavbarHighlights;
  setNavbarHighlights: Dispatch<SetStateAction<NavbarHighlights>>;
}>({
  navbarHighlights: {
    home: true,
    seeTogether: false,
    addMapOrPin: false,
    favorite: false,
    profile: false,
  },
  setNavbarHighlights: () => {},
});

function NavbarHighlightsProvider({ children }: NavbarHighlightsProviderProps) {
  const [navbarHighlights, setNavbarHighlights] = useState<NavbarHighlights>({
    home: true,
    seeTogether: false,
    addMapOrPin: false,
    favorite: false,
    profile: false,
  });

  return (
    <NavbarHighlightsContext.Provider
      value={{ navbarHighlights, setNavbarHighlights }}
    >
      {children}
    </NavbarHighlightsContext.Provider>
  );
}

export default NavbarHighlightsProvider;
