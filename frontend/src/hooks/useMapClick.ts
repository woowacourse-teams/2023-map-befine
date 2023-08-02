// hooks/useMapClick.tsx
import { useContext, useEffect } from 'react';
import { CoordinatesContext } from '../context/CoordinatesContext';

interface UseMapClickProps {
  map: Window['Tmapv2'] | null;
  getAddressFromServer: (lat: number, lng: number) => Promise<string>;
}

export default function useMapClick({
  map,
  getAddressFromServer,
}: UseMapClickProps) {
  const { setClickedCoordinate } = useContext(CoordinatesContext);

  useEffect(() => {
    if (!map) return;
    const clickHandler = async (evt: any) => {
      const roadName = await getAddressFromServer(
        evt.latLng._lat,
        evt.latLng._lng,
      );
      setClickedCoordinate({
        latitude: evt.latLng._lat,
        longitude: evt.latLng._lng,
        address: roadName,
      });
    };
    map.addListener('click', clickHandler);

    return () => {
      if (map) {
        map.removeListener('click', clickHandler);
      }
    };
  }, [map]);
}
