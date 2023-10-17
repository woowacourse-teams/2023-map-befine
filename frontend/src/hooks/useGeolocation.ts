import { useEffect, useRef, useState } from 'react';

import useToast from './useToast';

type GeoLocationState = {
  loaded: boolean;
  coordinates: { lat: string | number; lng: string | number };
  error?: { code: number; message: string };
};

const useGeoLocation = (mapInstance: TMap | null) => {
  const { Tmapv3 } = window;
  const { showToast } = useToast();
  const watchPositionId = useRef<number | null>(null);
  const [location, setLocation] = useState<GeoLocationState>({
    loaded: false,
    coordinates: { lat: '', lng: '' },
  });

  const focusMapCurrentLocation = () => {
    if (!mapInstance) return;
    if (location.coordinates.lat === '') return;

    mapInstance.setCenter(
      new Tmapv3.LatLng(
        Number(location.coordinates.lat),
        Number(location.coordinates.lng),
      ),
    );

    mapInstance.setZoom(17);
  };

  const onSuccess = (position: GeolocationPosition) => {
    setLocation({
      loaded: true,
      coordinates: {
        lat: position.coords.latitude,
        lng: position.coords.longitude,
      },
    });
  };

  const onError = (
    error: Pick<GeolocationPositionError, 'code' | 'message'>,
  ) => {
    setLocation({
      loaded: true,
      coordinates: { lat: '', lng: '' },
      error: {
        code: error.code,
        message: error.message,
      },
    });
    showToast('error', '위치 정보 사용을 허용해주세요.');
  };

  const requestUserLocation = () => {
    if (!('geolocation' in navigator)) {
      onError({ code: 0, message: 'Geolocation not supported' });
    }

    showToast('info', '위치 정보를 불러오는 중입니다.');

    navigator.geolocation.getCurrentPosition(onSuccess, onError);

    watchPositionId.current = navigator.geolocation.watchPosition(
      onSuccess,
      onError,
    );
  };

  useEffect(() => {
    focusMapCurrentLocation();
  }, [location]);

  useEffect(
    () => () => {
      if (!watchPositionId.current) return;

      navigator.geolocation.clearWatch(watchPositionId.current);
    },
    [],
  );

  return { location, requestUserLocation, focusMapCurrentLocation };
};

export default useGeoLocation;
