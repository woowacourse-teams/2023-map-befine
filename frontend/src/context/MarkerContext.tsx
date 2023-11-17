import { createContext, useContext, useState } from 'react';
import { useLocation, useParams } from 'react-router-dom';

import {
  getInfoWindowTemplate,
  pinColors,
  pinImageMap,
} from '../constants/pinImage';
import useNavigator from '../hooks/useNavigator';
import useMapStore from '../store/mapInstance';
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

type ElementType = 'marker' | 'infoWindow';

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
  const { topicId } = useParams<{ topicId: string }>();
  const { routePage } = useNavigator();
  const { pathname } = useLocation();

  const createElementsColor = (elementType: ElementType = 'marker') => {
    let markerType = -1;
    let currentTopicId = '-1';

    const colorMap = elementType === 'marker' ? pinImageMap : pinColors;

    const addedMarkerTypeCoordinates = coordinates.map((coordinate) => {
      if (coordinate.topicId === 'clustered') {
        markerType = -1;
      } else if (coordinate.topicId && currentTopicId !== coordinate.topicId) {
        markerType += 1;
        currentTopicId = coordinate.topicId;
      }

      return { ...coordinate, elementColor: colorMap[markerType + 1] };
    });

    return addedMarkerTypeCoordinates;
  };

  const createElementsInScreenSize = (elementType: ElementType) => {
    if (!mapInstance) return;

    const mapBounds = mapInstance.getBounds();
    const northEast = mapBounds._ne;
    const southWest = mapBounds._sw;

    const addedMarkerTypeCoordinates = createElementsColor(elementType);

    const coordinatesInScreenSize = addedMarkerTypeCoordinates.filter(
      (coordinate: any) =>
        coordinate.latitude <= northEast._lat &&
        coordinate.latitude >= southWest._lat &&
        coordinate.longitude <= northEast._lng &&
        coordinate.longitude >= southWest._lng,
    );

    return coordinatesInScreenSize;
  };

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
    const coordinatesInScreenSize = createElementsInScreenSize('marker');

    if (!coordinatesInScreenSize) return;

    const newMarkers = coordinatesInScreenSize.map((coordinate: any) => {
      const marker = new Tmapv3.Marker({
        position: new Tmapv3.LatLng(coordinate.latitude, coordinate.longitude),
        iconHTML: coordinate.elementColor,
        map: mapInstance,
      });
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

  const getCondition = (pins: any) => {
    if (!mapInstance) return;

    if (mapInstance.getZoom() === 17 && pins.length > 1) {
      return pins.length;
    }

    return 1;
  };

  const createInfowindows = () => {
    const coordinatesInScreenSize = createElementsInScreenSize('infoWindow');

    if (!coordinatesInScreenSize) return;

    const newInfowindows = coordinatesInScreenSize.map((coordinate: any) => {
      const infoWindow = new Tmapv3.InfoWindow({
        position: new Tmapv3.LatLng(coordinate.latitude, coordinate.longitude),
        border: 0,
        background: 'transparent',
        content: getInfoWindowTemplate({
          backgroundColor: coordinate.elementColor,
          pinName: coordinate.pinName,
          pins: coordinate.pins,
          condition: getCondition(coordinate.pins),
        }),
        offset: new Tmapv3.Point(0, -64),
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
    infoWindows?.forEach((infoWindow: InfoWindow) => {
      infoWindow.setMap(null);
    });
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
