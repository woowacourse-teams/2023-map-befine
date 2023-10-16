import { useContext, useLayoutEffect, useRef, useState } from 'react';
import { styled } from 'styled-components';

import CurrentLocation from '../../assets/currentLocationBtn.svg';
import { LayoutWidthContext } from '../../context/LayoutWidthContext';
import { MarkerContext } from '../../context/MarkerContext';
import useAnimateClickedPin from '../../hooks/useAnimateClickedPin';
import useClickedCoordinate from '../../hooks/useClickedCoordinate';
import useFocusToMarker from '../../hooks/useFocusToMarkers';
import useGeoLocation from '../../hooks/useGeolocation';
import useMapClick from '../../hooks/useMapClick';
import useUpdateCoordinates from '../../hooks/useUpdateCoordinates';
import Flex from '../common/Flex';

function Map() {
  const { Tmapv3 } = window;

  const { markers } = useContext(MarkerContext);
  const { width } = useContext(LayoutWidthContext);
  const [mapInstance, setMapInstance] = useState<TMap | null>(null);

  const mapContainer = useRef(null);
  const location = useGeoLocation();

  const handleCurrentLocationClick = () => {
    if (!location.loaded) {
      console.log('위치 정보를 불러오는 중입니다...');
      return;
    }

    if (location.error) {
      console.error(
        '위치 정보를 불러오는데 실패했습니다:',
        location.error.message,
      );
      return;
    }

    if (mapInstance) {
      mapInstance.setCenter(
        new Tmapv3.LatLng(
          Number(location.coordinates.lat),
          Number(location.coordinates.lng),
        ),
      );
      mapInstance.setZoom(15);
    }
  };

  useLayoutEffect(() => {
    if (!Tmapv3 || !mapContainer.current) return;

    const map = new Tmapv3.Map(mapContainer.current, {
      center: new Tmapv3.LatLng(37.5154, 127.1029),
      scaleBar: false,
    });

    if (!map) return;

    map.setZoomLimit(7, 17);

    setMapInstance(map);

    // eslint-disable-next-line consistent-return
    return () => {
      map.destroy();
    };
  }, []);

  useMapClick(mapInstance);
  useClickedCoordinate(mapInstance);
  useUpdateCoordinates(mapInstance);

  useFocusToMarker(mapInstance, markers);
  useAnimateClickedPin(mapInstance, markers);

  return (
    <MapContainer>
      <MapFlex
        aria-label="괜찮을지도 지도 이미지"
        flex="1"
        id="map"
        ref={mapContainer}
        height="calc(var(--vh, 1vh) * 100)"
        $minWidth={width}
      />
      <CurrentLocationIcon onClick={handleCurrentLocationClick} />
    </MapContainer>
  );
}

const MapContainer = styled.div`
  position: relative;
`;

const MapFlex = styled(Flex)`
  & {
    canvas {
      min-width: ${({ $minWidth }) =>
        $minWidth === '100vw' ? '0' : 'calc(100vw - 400px)'};
    }
  }

  @media (max-width: 1076px) {
    max-height: calc(var(--vh, 1vh) * 50);
  }
`;

const CurrentLocationIcon = styled(CurrentLocation)`
  position: absolute;
  cursor: pointer;
  bottom: 32px;
  right: 12px;
  width: 40px;
  height: 40px;
  z-index: 10;
`;

export default Map;
