import { createContext, Dispatch, SetStateAction, useState } from 'react';

export interface Coordinate {
  latitude: number;
  longitude: number;
}

export interface CoordinatesContextType {
  coordinates: Coordinate[];
  setCoordinates: Dispatch<SetStateAction<Coordinate[]>>;
}

export const CoordinatesContext = createContext<CoordinatesContextType>({
  coordinates: [],
  setCoordinates: () => {},
});

interface Props {
  children: JSX.Element | JSX.Element[];
}

const CoordinatesProvider = ({ children }: Props): JSX.Element => {
  const [coordinates, setCoordinates] = useState([
    { latitude: 37.5055, longitude: 127.0509 },
  ]);

  return (
    <CoordinatesContext.Provider
      value={{
        coordinates,
        setCoordinates,
      }}
    >
      {children}
    </CoordinatesContext.Provider>
  );
};

export default CoordinatesProvider;
