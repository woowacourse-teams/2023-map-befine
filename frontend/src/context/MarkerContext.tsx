import { createContext, useContext, useState } from 'react';
import { Coordinate, CoordinatesContext } from './CoordinatesContext';
import { useParams } from 'react-router-dom';
import useNavigator from '../hooks/useNavigator';
import { pinColors, pinImageMap } from '../constants/pinImage';

type MarkerContextType = {
  markers: Marker[];
  clickedMarker: Marker | null;
  createMarkers: (map: TMap) => void;
  removeMarkers: () => void;
  removeInfowindows: () => void;
  createInfowindows: (map: TMap) => void;
  displayClickedMarker: (map: TMap) => void;
};

const defaultMarkerContext = () => {
  throw new Error('MarkerContext가 제공되지 않았습니다.');
};

export const MarkerContext = createContext<MarkerContextType>({
  markers: [],
  clickedMarker: null,
  createMarkers: defaultMarkerContext,
  removeMarkers: defaultMarkerContext,
  removeInfowindows: defaultMarkerContext,
  createInfowindows: defaultMarkerContext,
  displayClickedMarker: defaultMarkerContext,
});

interface Props {
  children: JSX.Element | JSX.Element[];
}

const MarkerProvider = ({ children }: Props): JSX.Element => {
  const { Tmapv3 } = window;
  const [markers, setMarkers] = useState<Marker[]>([]);
  const [infoWindows, setInfoWindows] = useState<InfoWindow[] | null>(null);
  const [clickedMarker, setClickedMarker] = useState<Marker | null>(null);
  const { coordinates, clickedCoordinate } = useContext(CoordinatesContext);
  const { topicId } = useParams<{ topicId: string }>();
  const { routePage } = useNavigator();

  const createMarker = (
    coordinate: Coordinate,
    map: TMap,
    markerType: number,
  ) => {
    return new Tmapv3.Marker({
      position: new Tmapv3.LatLng(coordinate.latitude, coordinate.longitude),
      icon: pinImageMap[markerType + 1],
      map,
    });
  };

  // 현재 클릭된 좌표의 마커 생성
  const displayClickedMarker = (map: TMap) => {
    if (clickedMarker) {
      clickedMarker.setMap(null);
    }
    const marker = new Tmapv3.Marker({
      position: new Tmapv3.LatLng(
        clickedCoordinate.latitude,
        clickedCoordinate.longitude,
      ),
      icon: 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_g_m_m.png',
      map,
    });
    marker.id = 'clickedMarker';
    setClickedMarker(marker);
  };

  //coordinates를 받아서 marker를 생성하고, marker를 markers 배열에 추가
  const createMarkers = (map: TMap) => {
    let markerType = -1;
    let currentTopicId = '-1';

    let newMarkers = coordinates.map((coordinate: any) => {
      if (currentTopicId !== coordinate.topicId) {
        markerType = (markerType + 1) % 7;
        currentTopicId = coordinate.topicId;
      }
      let marker = createMarker(coordinate, map, markerType);
      marker.id = String(coordinate.id);
      return marker;
    });

    newMarkers.forEach((marker: Marker) => {
      marker.on('click', () => {
        routePage(`/topics/${topicId}?pinDetail=${marker.id}`);
      });
    });
    setMarkers(newMarkers);
  };

  const createInfowindows = (map: TMap) => {
    let markerType = -1;
    let currentTopicId = '-1';

    const newInfowindows = coordinates.map((coordinate: any) => {
      if (currentTopicId !== coordinate.topicId) {
        markerType = (markerType + 1) % 7;
        currentTopicId = coordinate.topicId;
      }

      const infoWindow = new Tmapv3.InfoWindow({
        position: new Tmapv3.LatLng(coordinate.latitude, coordinate.longitude),

        content: `<div style="padding: 4px 12px; display:flex; justify-contents: center; align-items: center; height:32px; font-size:14px; color:#ffffff; background-color: ${
          pinColors[markerType + 1]
        };" >${coordinate.pinName}</div>`,
        offset: new Tmapv3.Point(0, -60),
        type: 2,
        map: map,
      });
      return infoWindow;
    });

    setInfoWindows(newInfowindows);
  };

  const removeMarkers = () => {
    markers?.forEach((marker: Marker) => marker.setMap(null));
    setMarkers([]);
  };

  const removeInfowindows = () => {
    infoWindows?.forEach((infoWindow: InfoWindow) => infoWindow.setMap(null));
    setInfoWindows([]);
  };

  return (
    <MarkerContext.Provider
      value={{
        clickedMarker,
        markers,
        removeMarkers,
        removeInfowindows,
        createInfowindows,
        createMarkers,
        displayClickedMarker,
      }}
    >
      {children}
    </MarkerContext.Provider>
  );
};

export default MarkerProvider;
