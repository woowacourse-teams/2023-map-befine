import { useContext, useEffect, useRef, useState } from 'react';
import { styled } from 'styled-components';

import CurrentLocation from '../../assets/currentLocationBtn.svg';
import { LayoutWidthContext } from '../../context/LayoutWidthContext';
import { MarkerContext } from '../../context/MarkerContext';
import useAnimateClickedPin from '../../hooks/useAnimateClickedPin';
import useClickedCoordinate from '../../hooks/useClickedCoordinate';
import useFocusToMarker from '../../hooks/useFocusToMarkers';
import useGeoLocation from '../../hooks/useGeolocation';
import useMapClick from '../../hooks/useMapClick';
import useToast from '../../hooks/useToast';
import useUpdateCoordinates from '../../hooks/useUpdateCoordinates';
import useMapStore from '../../store/mapInstance';
import Flex from '../common/Flex';

const MOBILE_WIDTH = 744;

const getZoomMinLimit = () => {
  if (window.innerWidth <= MOBILE_WIDTH) return 6;
  return 7;
};

function Map() {
  const { Tmapv3 } = window;

  const { markers } = useContext(MarkerContext);
  const { width } = useContext(LayoutWidthContext);
  const { mapInstance, setMapInstance } = useMapStore((state) => state);

  const mapContainer = useRef(null);
  const { location, isUsingUserLocation, requestUserLocation } =
    useGeoLocation(mapInstance);
  const { showToast } = useToast();

  const handleCurrentLocationClick = () => {
    if (location.error) {
      showToast('error', '위치 정보 사용을 허용해주세요.');
      return;
    }

    requestUserLocation();
  };

  useEffect(() => {
    if (!Tmapv3 || !mapContainer.current) return;

    const map = new Tmapv3.Map(mapContainer.current, {
      center: new Tmapv3.LatLng(37.5154, 127.1029),
      scaleBar: false,
    });

    if (!map) return;

    map.setZoomLimit(getZoomMinLimit(), 17);

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
      <CurrentLocationIcon
        onClick={handleCurrentLocationClick}
        $isUsingUserLocation={isUsingUserLocation}
      />
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
        $minWidth === '100vw' ? '0' : 'calc(100vw - 372px)'};
    }
  }

  @media (max-width: 1076px) {
    max-height: calc(var(--vh, 1vh) * 50);
  }
`;

const CurrentLocationIcon = styled(CurrentLocation)<{
  $isUsingUserLocation: boolean;
}>`
  position: absolute;
  cursor: pointer;
  bottom: 8%;
  right: 4%;
  width: 52px;
  height: 52px;
  z-index: 10;
  opacity: 0.85;
  filter: ${({ $isUsingUserLocation }) =>
    $isUsingUserLocation && 'brightness(0.6)'};

  @media (max-width: 1036px) {
    bottom: 8%;
    right: 1%;
  }
`;

export default Map;
