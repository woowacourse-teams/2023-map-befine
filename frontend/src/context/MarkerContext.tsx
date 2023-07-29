import { createContext, useContext, useEffect, useState } from 'react';
import { CoordinatesContext } from './CoordinatesContext';
import { useParams } from 'react-router-dom';
import useNavigator from '../hooks/useNavigator';

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
  const { topicId } = useParams<{ topicId: string }>();
  const { routePage } = useNavigator();

  // 핀 추가하기 페이지에서 마커의 애니메이션 제거
  useEffect(() => {
    if (location.pathname === '/new-pin') {
      if (markers.length > 0) {
        removeAnimation();
      }
    }
  }, [location.pathname]);

  //coordinates를 받아서 marker를 생성하고, marker를 markers 배열에 추가
  const createMarkers = (map: any) => {
    const newMarkers = coordinates.map((coordinate: any) => {
      let tag = '';
      if (!topicId) {
        tag = 'A';
      } else {
        tag = String.fromCharCode(97 + parseInt(topicId, 10));
      }
      const marker = new window.Tmapv2.Marker({
        position: new window.Tmapv2.LatLng(
          coordinate.latitude,
          coordinate.longitude,
        ),
        icon: `http://tmapapi.sktelecom.com/upload/tmap/marker/pin_g_b_${tag}.png`,
        map,
      });
      marker.id = coordinate.id;
      return marker;
    });

    //newMarkers 각각에 onClick 이벤트를 추가
    newMarkers.forEach((marker: any) => {
      marker.addListener('click', () => {
        routePage(`/topics/${topicId}?pinDetail=${marker.id}`);
      });
    });

    setMarkers(newMarkers);
  };

  const removeMarkers = () => {
    markers.forEach((marker: any) => marker.setMap(null));
    setMarkers([]);
  };

  //마커 에니메이션 제거
  const removeAnimation = () => {
    markers.forEach((marker: any) => {
      marker._marker_data.options.animationLength = null;
    });
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
