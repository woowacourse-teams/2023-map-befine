import { useContext, useEffect, useRef } from 'react';

import { CoordinatesContext } from '../../../context/CoordinatesContext';
import useMapStore from '../../../store/mapInstance';

interface Props {
  topicId: string;
  onAfterInteraction: () => void;
}

const useHandleMapInteraction = ({ topicId, onAfterInteraction }: Props) => {
  const { mapInstance } = useMapStore((state) => state);

  const zoomTimerIdRef = useRef<NodeJS.Timeout | null>(null);
  const dragTimerIdRef = useRef<NodeJS.Timeout | null>(null);
  const { setCoordinates } = useContext(CoordinatesContext);

  const setPrevCoordinates = () => {
    setCoordinates((prev) => [...prev]);
  };

  const adjustMapDirection = () => {
    if (!mapInstance) return;

    mapInstance.setBearing(0);
    mapInstance.setPitch(0);
  };

  useEffect(() => {
    onAfterInteraction();

    const onDragEnd = (evt: evt) => {
      if (dragTimerIdRef.current) {
        clearTimeout(dragTimerIdRef.current);
      }

      dragTimerIdRef.current = setTimeout(() => {
        setPrevCoordinates();
        adjustMapDirection();
      }, 100);
    };
    const onZoomEnd = (evt: evt) => {
      if (zoomTimerIdRef.current) {
        clearTimeout(zoomTimerIdRef.current);
      }

      zoomTimerIdRef.current = setTimeout(() => {
        onAfterInteraction();
        adjustMapDirection();
      }, 100);
    };

    if (!mapInstance) return;

    mapInstance.on('DragEnd', onDragEnd);
    mapInstance.on('ZoomEnd', onZoomEnd);

    return () => {
      mapInstance.off('DragEnd', onDragEnd);
      mapInstance.off('ZoomEnd', onZoomEnd);
    };
  }, [topicId, mapInstance]);
};

export default useHandleMapInteraction;
