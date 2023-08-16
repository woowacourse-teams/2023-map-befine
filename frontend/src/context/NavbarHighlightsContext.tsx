import {
  Dispatch,
  ReactNode,
  SetStateAction,
  createContext,
  useState,
} from 'react';

interface NavbarHighlightsProps {
  home: boolean;
  seeTogether: boolean;
  addMapOrPin: boolean;
  favorite: boolean;
  profile: boolean;
}

interface NavbarHighlightsContextProps {
  navbarHighlights: NavbarHighlightsProps;
  setNavbarHighlights: Dispatch<SetStateAction<NavbarHighlightsProps>>;
}

interface NavbarHighlightsProviderProps {
  children: ReactNode;
}

export const NavbarHighlightsContext =
  createContext<NavbarHighlightsContextProps>({
    navbarHighlights: {
      home: true,
      seeTogether: false,
      addMapOrPin: false,
      favorite: false,
      profile: false,
    },
    setNavbarHighlights: () => {},
  });

const NavbarHighlightsProvider = ({
  children,
}: NavbarHighlightsProviderProps) => {
  const [navbarHighlights, setNavbarHighlights] = useState({
    home: true,
    seeTogether: false,
    addMapOrPin: false,
    favorite: false,
    profile: false,
  });

  return (
    <NavbarHighlightsContext.Provider
      value={{
        navbarHighlights,
        setNavbarHighlights,
      }}
    >
      {children}
    </NavbarHighlightsContext.Provider>
  );
};

export default NavbarHighlightsProvider;
