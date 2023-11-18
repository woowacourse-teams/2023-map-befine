import { useEffect, useState } from 'react';

import { USER_LOCATION_IMAGE } from '../constants/pinImage';
import useToast from './useToast';

type GeoLocationState = {
  coordinates: { lat: string | number; lng: string | number };
  error?: { code: number; message: string };
};

const INIT_VALUE = '';

const useGeoLocation = (mapInstance: TMap | null) => {
  const { Tmapv3 } = window;
  const { showToast } = useToast();
  const [userMarker, setUserMarker] = useState<Marker | null>(null);
  const [isRequesting, setIsRequesting] = useState<boolean>(false);
  const [location, setLocation] = useState<GeoLocationState>({
    coordinates: { lat: '', lng: '' },
  });

  const removeUserMarker = () => {
    setLocation({
      coordinates: { lat: INIT_VALUE, lng: INIT_VALUE },
    });

    if (userMarker) {
      userMarker.setMap(null);
      setUserMarker(null);
    }
  };

  const createUserMarkerWithZoomingMap = () => {
    if (!mapInstance) return;

    const userCoordinates = new Tmapv3.LatLng(
      Number(location.coordinates.lat),
      Number(location.coordinates.lng),
    );

    mapInstance.setCenter(userCoordinates);
    mapInstance.setZoom(17);

    const userPositionMarker = new Tmapv3.Marker({
      position: userCoordinates,
      iconHTML: USER_LOCATION_IMAGE,
      map: mapInstance,
    });

    setUserMarker(userPositionMarker);
  };

  const onSuccess = (position: GeolocationPosition) => {
    setLocation({
      coordinates: {
        lat: position.coords.latitude,
        lng: position.coords.longitude,
      },
    });

    setIsRequesting(false);
  };

  const onError = (
    error: Pick<GeolocationPositionError, 'code' | 'message'>,
  ) => {
    setLocation({
      coordinates: { lat: INIT_VALUE, lng: INIT_VALUE },
      error: {
        code: error.code,
        message: error.message,
      },
    });

    showToast(
      'error',
      '현재 위치를 사용하려면 설정에서 위치 권한을 허용해주세요.',
    );
    setIsRequesting(false);
  };

  const requestUserLocation = () => {
    if (!('geolocation' in navigator)) {
      onError({ code: 0, message: 'Geolocation not supported' });
    }

    if (isRequesting) {
      showToast('info', '현재 위치를 찾고 있어요.');
      return;
    }

    removeUserMarker();

    showToast('info', '현재 위치를 불러옵니다.');

    navigator.geolocation.getCurrentPosition(onSuccess, onError);

    setIsRequesting(true);
  };

  useEffect(() => {
    if (location.coordinates.lat === INIT_VALUE) {
      return;
    }

    createUserMarkerWithZoomingMap();
  }, [location]);

  return {
    location,
    requestUserLocation,
  };
};

export default useGeoLocation;
