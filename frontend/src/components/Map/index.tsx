import { useContext, useLayoutEffect, useRef, useState } from 'react';
import Flex from '../common/Flex';
import { MarkerContext } from '../../context/MarkerContext';
import useMapClick from '../../hooks/useMapClick';
import useClickedCoordinate from '../../hooks/useClickedCoordinate';
import useUpdateCoordinates from '../../hooks/useUpdateCoordinates';
import useFocusToMarker from '../../hooks/useFocusToMarkers';
import useAnimateClickedPin from '../../hooks/useAnimateClickedPin';
import { styled } from 'styled-components';
import { LayoutWidthContext } from '../../context/LayoutWidthContext';

const Map = () => {
  const { Tmapv3 } = window;

  const { markers } = useContext(MarkerContext);
  const { width } = useContext(LayoutWidthContext);
  const [mapInstance, setMapInstance] = useState<TMap>(null!);

  const mapContainer = useRef(null);

  useLayoutEffect(() => {
    if (!Tmapv3 || !mapContainer.current) return;

    const map = new Tmapv3.Map(mapContainer.current, {
      center: new Tmapv3.LatLng(37.5154, 127.1029),
    });

    if (!map) return;

    map.setZoomLimit(7, 17);

    setMapInstance(map);

    return () => {
      map.destroy();
    };
  }, []);

  useMapClick(mapInstance);
  useClickedCoordinate(mapInstance);
  useUpdateCoordinates(mapInstance!);

  useFocusToMarker(mapInstance, markers);
  useAnimateClickedPin(mapInstance, markers);

  return (
    <MapFlex
      aria-label="괜찮을지도 지도 이미지"
      flex="1"
      id="map"
      ref={mapContainer}
      height="100vh"
      $minWidth={width}
    />
  );
};

const MapFlex = styled(Flex)`
  & {
    canvas {
      min-width: ${({ $minWidth }) =>
        $minWidth === '100vw' ? '0' : 'calc(100vw - 400px)'};
    }
  }
`;

export default Map;
