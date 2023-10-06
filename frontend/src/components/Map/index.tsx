import { useContext, useLayoutEffect, useRef, useState } from 'react';
import { styled } from 'styled-components';

import { LayoutWidthContext } from '../../context/LayoutWidthContext';
import { MarkerContext } from '../../context/MarkerContext';
import useAnimateClickedPin from '../../hooks/useAnimateClickedPin';
import useClickedCoordinate from '../../hooks/useClickedCoordinate';
import useFocusToMarker from '../../hooks/useFocusToMarkers';
import useMapClick from '../../hooks/useMapClick';
import useUpdateCoordinates from '../../hooks/useUpdateCoordinates';
import Flex from '../common/Flex';

function Map() {
  const { Tmapv3 } = window;

  const { markers } = useContext(MarkerContext);
  const { width } = useContext(LayoutWidthContext);
  const [mapInstance, setMapInstance] = useState<TMap | null>(null);

  const mapContainer = useRef(null);

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
    <MapFlex
      aria-label="괜찮을지도 지도 이미지"
      flex="1"
      id="map"
      ref={mapContainer}
      height="calc(var(--vh, 1vh) * 100)"
      $minWidth={width}
    />
  );
}

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

export default Map;
