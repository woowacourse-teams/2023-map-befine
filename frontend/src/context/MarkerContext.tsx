import { createContext, useContext, useState } from 'react';
import { useLocation, useParams } from 'react-router-dom';

import { pinColors, pinImageMap } from '../constants/pinImage';
import useNavigator from '../hooks/useNavigator';
import useMapStore from '../store/mapInstance';
import useMapSidebarCoordinates from '../store/mapSidebarCoordinates';
import { Coordinate, CoordinatesContext } from './CoordinatesContext';

type MarkerContextType = {
  markers: Marker[];
  clickedMarker: Marker | null;
  createMarkers: () => void;
  removeMarkers: () => void;
  removeInfowindows: () => void;
  createInfowindows: () => void;
  displayClickedMarker: () => void;
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

function MarkerProvider({ children }: Props): JSX.Element {
  const { Tmapv3 } = window;
  const { mapInstance } = useMapStore((state) => state);
  const [markers, setMarkers] = useState<Marker[]>([]);
  const [infoWindows, setInfoWindows] = useState<InfoWindow[] | null>(null);
  const [clickedMarker, setClickedMarker] = useState<Marker | null>(null);
  const { coordinates, clickedCoordinate } = useContext(CoordinatesContext);
  const { sidebarCoordinates } = useMapSidebarCoordinates((state) => state);
  const { topicId } = useParams<{ topicId: string }>();
  const { routePage } = useNavigator();
  const { pathname } = useLocation();

  const createElementsInScreenSize = () => {
    if (!mapInstance) return;

    const mapBounds = mapInstance.getBounds();
    const northEast = mapBounds._ne;
    const southWest = mapBounds._sw;

    const coordinatesInScreenSize = coordinates.filter(
      (coordinate: any) =>
        coordinate.latitude <= northEast._lat &&
        coordinate.latitude >= southWest._lat &&
        coordinate.longitude <= northEast._lng &&
        coordinate.longitude >= southWest._lng,
    );

    return coordinatesInScreenSize;
  };

  const createMarker = (coordinate: Coordinate, markerType: number) =>
    new Tmapv3.Marker({
      position: new Tmapv3.LatLng(coordinate.latitude, coordinate.longitude),
      iconHTML: pinImageMap[markerType + 1],
      map: mapInstance,
    });

  // 현재 클릭된 좌표의 마커 생성
  const displayClickedMarker = () => {
    if (clickedMarker) {
      clickedMarker.setMap(null);
    }
    const marker = new Tmapv3.Marker({
      position: new Tmapv3.LatLng(
        clickedCoordinate.latitude,
        clickedCoordinate.longitude,
      ),
      icon: 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_g_m_m.png',
      map: mapInstance,
    });
    marker.id = 'clickedMarker';
    setClickedMarker(marker);
  };

  // coordinates를 받아서 marker를 생성하고, marker를 markers 배열에 추가
  const createMarkers = () => {
    let markerType = -1;
    let currentTopicId = '-1';

    const markersInScreenSize = createElementsInScreenSize();

    if (!markersInScreenSize) return;

    const newMarkers = markersInScreenSize.map((coordinate: any) => {
      if (currentTopicId !== coordinate.topicId) {
        markerType = (markerType + 1) % 7;
        currentTopicId = coordinate.topicId;
      }
      const marker = createMarker(coordinate, markerType);
      marker.id = String(coordinate.id);
      return marker;
    });

    newMarkers.forEach((marker: Marker) => {
      marker.on('click', () => {
        if (pathname.split('/')[1] === 'topics') {
          routePage(`/topics/${topicId}?pinDetail=${marker.id}`);
          return;
        }

        routePage(`/see-together/${topicId}?pinDetail=${marker.id}`);
      });
    });

    setMarkers(newMarkers);
  };

  const createInfowindows = () => {
    let markerType = -1;
    let currentTopicId = '-1';

    const windowsInScreenSize = createElementsInScreenSize();

    if (!windowsInScreenSize) return;

    const newInfowindows = windowsInScreenSize.map((coordinate: any) => {
      if (currentTopicId !== coordinate.topicId) {
        markerType = (markerType + 1) % 7;
        currentTopicId = coordinate.topicId;
      }

      const infoWindow = new Tmapv3.InfoWindow({
        position: new Tmapv3.LatLng(coordinate.latitude, coordinate.longitude),
        border: 0,
        background: 'transparent',
        content: `<div style="padding: 4px 12px; display:flex; border-radius: 20px; justify-contents: center; align-items: center; height:32px; font-size:14px; color:#ffffff; background-color: ${
          pinColors[markerType + 1]
        };">${coordinate.pinName}</div>`,
        offset: new Tmapv3.Point(0, -60),
        type: 2,
        map: mapInstance,
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
}

export default MarkerProvider;
