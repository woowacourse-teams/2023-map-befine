import { createContext, useContext, useEffect, useState } from 'react';
import { CoordinatesContext } from './CoordinatesContext';
import { useParams } from 'react-router-dom';
import useNavigator from '../hooks/useNavigator';

type MarkerContextType = {
  markers: any[];
  clickedMarker: any;
  createMarkers: (map: any) => void;
  removeMarkers: () => void;
  displayClickedMarker: (map: any) => void;
};

export const MarkerContext = createContext<MarkerContextType>({
  markers: [],
  clickedMarker: null,
  createMarkers: () => {},
  removeMarkers: () => {},
  displayClickedMarker: () => {},
});

interface Props {
  children: JSX.Element | JSX.Element[];
}

const MarkerProvider = ({ children }: Props): JSX.Element => {
  const [markers, setMarkers] = useState<any>([]);
  const [clickedMarker, setClickedMarker] = useState<any>(null);
  const { coordinates, clickedCoordinate } = useContext(CoordinatesContext);
  const { topicId } = useParams<{ topicId: string }>();
  const { routePage } = useNavigator();

  // 현재 클릭된 좌표의 마커 생성
  const displayClickedMarker = (map: any) => {
    if (clickedMarker) {
      clickedMarker.setMap(null);
    }
    const marker = new window.Tmapv2.Marker({
      position: new window.Tmapv2.LatLng(
        clickedCoordinate.latitude,
        clickedCoordinate.longitude,
      ),
      icon: 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_g_b_a.png',
      map,
    });
    marker.id = 'clickedMarker';
    setClickedMarker(marker);
  };

  //coordinates를 받아서 marker를 생성하고, marker를 markers 배열에 추가
  const createMarkers = (map: any) => {
    const newMarkers = coordinates.map((coordinate: any) => {
      let tag = '';
      if (!topicId) {
        tag = 'A';
      } else {
        tag = String.fromCharCode(97 + (parseInt(coordinate.topicId, 10) % 26));
      }
      const marker = new window.Tmapv2.Marker({
        position: new window.Tmapv2.LatLng(
          coordinate.latitude,
          coordinate.longitude,
        ),
        icon: `http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_${tag}.png`,
        map,
      });
      marker.id = String(coordinate.id);
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
        clickedMarker,
        markers,
        removeMarkers,
        createMarkers,
        displayClickedMarker,
      }}
    >
      {children}
    </MarkerContext.Provider>
  );
};

export default MarkerProvider;
