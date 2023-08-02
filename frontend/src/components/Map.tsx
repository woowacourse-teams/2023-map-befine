import { forwardRef, useContext } from 'react';
import Flex from './common/Flex';
import { MarkerContext } from '../context/MarkerContext';
import useMapClick from '../hooks/useMapClick';
import useClickedCoordinate from '../hooks/useClickedCoordinate';
import useUpdateCoordinates from '../hooks/useUpdateCoordinates';
import useFocusToMarker from '../hooks/useFocusToMarkers';
import useAnimateClickedPin from '../hooks/useAnimateClickedPin';

const Map = (props: any, ref: any) => {
  const { map } = props;
  const { markers } = useContext(MarkerContext);

  useMapClick(map);
  useClickedCoordinate(map);
  useUpdateCoordinates(map);

  useFocusToMarker(map, markers);
  useAnimateClickedPin(map, markers);

  return <Flex flex="1" id="map" ref={ref} height="100vh" width="100%" />;
};

export default forwardRef(Map);
