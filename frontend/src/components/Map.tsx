import { useEffect, useRef } from 'react';
import Box from './common/Box';

declare global {
  interface Window {
    Tmapv2: any;
  }
}

const Map = () => {
  const mapContainer = useRef<HTMLDivElement>(null);
  useEffect(() => {
    const map = new window.Tmapv2.Map(mapContainer.current, {
      center: new window.Tmapv2.LatLng(37.5055, 127.0509),
      zoom: 15,
    });
  }, []);

  return (
    <Box
      id="map"
      ref={mapContainer}
      style={{
        height: '100vh',
        width: 'calc(100vw - 400px)',
      }}
    ></Box>
  );
};

export default Map;
