import {
  Dispatch,
  SetStateAction,
  createContext,
  useContext,
  useState,
} from 'react';
import { CoordinatesContext } from './CoordinatesContext';
import { c } from 'msw/lib/glossary-de6278a9';

type MarkerContextType = {
  markers: any[];
  setMarkers: Dispatch<SetStateAction<any[]>>;
};

export const MarkerContext = createContext<MarkerContextType>({
  markers: [],
  setMarkers: () => {},
});

interface Props {
  children: JSX.Element | JSX.Element[];
}

const MarkerProvider = ({ children }: Props): JSX.Element => {
  const [markers, setMarkers] = useState<any>([]);
  const { coordinates } = useContext(CoordinatesContext);

  //coordinates를 받아서 marker를 생성하고, marker를 markers 배열에 추가
  const createMarkers = (map: any) => {
    const newMarkers = coordinates.map((coordinate: any) => {
      return new window.Tmapv2.Marker({
        position: new window.Tmapv2.LatLng(
          coordinate.latitude,
          coordinate.longitude,
        ),
        icon: 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_1.png',
        map,
      });
    });
    setMarkers(newMarkers);
  };
  //coordinates가 바뀔 때마다 markers 배열을 초기화하고 새로운 marker들을 생성
  //coordinates가 바뀔 때마다 지도의 중심과 확대 정도를 조절

  return (
    <MarkerContext.Provider
      value={{
        markers,
        setMarkers,
      }}
    >
      {children}
    </MarkerContext.Provider>
  );
};

export default MarkerProvider;
