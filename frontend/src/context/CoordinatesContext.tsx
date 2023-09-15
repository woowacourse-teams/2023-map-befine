import {
  createContext,
  Dispatch,
  SetStateAction,
  useEffect,
  useState,
} from 'react';

export interface Coordinate {
  latitude: number;
  longitude: number;
  address?: string;
  topicId?: string;
}

export interface CoordinatesContextType {
  coordinates: Coordinate[];
  setCoordinates: Dispatch<SetStateAction<Coordinate[]>>;
  clickedCoordinate: Coordinate;
  setClickedCoordinate: Dispatch<SetStateAction<Coordinate>>;
}

export const CoordinatesContext = createContext<CoordinatesContextType>({
  coordinates: [],
  setCoordinates: () => {},
  clickedCoordinate: { latitude: 0, longitude: 0, address: '' },
  setClickedCoordinate: () => {},
});

interface Props {
  children: JSX.Element | JSX.Element[];
}

const CoordinatesProvider = ({ children }: Props): JSX.Element => {
  const [coordinates, setCoordinates] = useState<Coordinate[]>([
    { latitude: 37.5055, longitude: 127.0509 },
  ]);
  const [clickedCoordinate, setClickedCoordinate] = useState<Coordinate>({
    latitude: 0,
    longitude: 0,
    address: '',
  });

  // new-pin페이지가 아닌 경우 address를 =''로 고정
  useEffect(() => {
    if (location.pathname !== '/new-pin') {
      setClickedCoordinate((prevState) => ({ ...prevState, address: '' }));
    }
  }, [location.pathname]);

  return (
    <CoordinatesContext.Provider
      value={{
        coordinates,
        setCoordinates,
        clickedCoordinate,
        setClickedCoordinate,
      }}
    >
      {children}
    </CoordinatesContext.Provider>
  );
};

export default CoordinatesProvider;
