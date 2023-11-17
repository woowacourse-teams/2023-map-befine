import { PIN_SIZE } from '../constants';

const useRealDistanceOfPin = () => {
  const { Tmapv3 } = window;

  const getDistanceOfPin = (mapInstance: TMap) => {
    const mapBounds = mapInstance.getBounds();

    const leftWidth = new Tmapv3.LatLng(mapBounds._ne._lat, mapBounds._sw._lng);
    const rightWidth = new Tmapv3.LatLng(
      mapBounds._ne._lat,
      mapBounds._ne._lng,
    );

    const realDistanceOfScreen = leftWidth.distanceTo(rightWidth);
    const currentScreenSize =
      mapInstance.realToScreen(rightWidth).x -
      mapInstance.realToScreen(leftWidth).x;

    return (realDistanceOfScreen / currentScreenSize) * PIN_SIZE;
  };

  return { getDistanceOfPin };
};

export default useRealDistanceOfPin;
