import { createContext, useContext, useState } from 'react';
import { CoordinatesContext } from './CoordinatesContext';

type MarkerContextType = {
  markers: any[];
  createMarkers: (map: any) => void;
  removeMarkers: () => void;
};

export const MarkerContext = createContext<MarkerContextType>({
  markers: [],
  createMarkers: () => {},
  removeMarkers: () => {},
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

  const removeMarkers = () => {
    markers.forEach((marker: any) => marker.setMap(null));
    setMarkers([]);
  };

  return (
    <MarkerContext.Provider
      value={{
        markers,
        removeMarkers,
        createMarkers,
      }}
    >
      {children}
    </MarkerContext.Provider>
  );
};

export default MarkerProvider;
