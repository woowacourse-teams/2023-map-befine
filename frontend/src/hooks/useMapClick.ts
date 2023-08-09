import { useContext, useEffect } from 'react';
import { CoordinatesContext } from '../context/CoordinatesContext';
import getAddressFromServer from '../lib/getAddressFromServer';

export default function useMapClick(map: any) {
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
    map.on('click', clickHandler);

    return () => {
      if (map) {
        map.removeListener('click', clickHandler);
      }
    };
  }, [map]);
}
