import { forwardRef, useContext } from 'react';
import Flex from '../common/Flex';
import { MarkerContext } from '../../context/MarkerContext';
import useMapClick from '../../hooks/useMapClick';
import useClickedCoordinate from '../../hooks/useClickedCoordinate';
import useUpdateCoordinates from '../../hooks/useUpdateCoordinates';
import useFocusToMarker from '../../hooks/useFocusToMarkers';
import useAnimateClickedPin from '../../hooks/useAnimateClickedPin';
import { styled } from 'styled-components';
import { LayoutWidthContext } from '../../context/LayoutWidthContext';

const Map = (props: any, ref: any) => {
  const { map } = props;
  const { markers } = useContext(MarkerContext);
  const { width } = useContext(LayoutWidthContext);

  useMapClick(map);
  useClickedCoordinate(map);
  useUpdateCoordinates(map);

  useFocusToMarker(map, markers);
  useAnimateClickedPin(map, markers);

  return (
    <MapFlex flex="1" id="map" ref={ref} height="100vh" $minWidth={width} />
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

export default forwardRef(Map);
