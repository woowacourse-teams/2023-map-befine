import { useEffect, useRef, useState } from 'react';

import { USER_LOCATION_IMAGE } from '../constants/pinImage';
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
  const [userMarker, setUserMarker] = useState<Marker | null>(null);
  const [isUsingUserLocation, setIsUsingUserLocation] = useState<boolean>(true);

  const removeUserMarker = () => {
    if (watchPositionId.current) {
      navigator.geolocation.clearWatch(watchPositionId.current);
    }

    userMarker?.setMap(null);
    setUserMarker(null);
  };

  const createUserMarkerWithZoomingMap = () => {
    if (!mapInstance) return;

    if (!isUsingUserLocation) {
      removeUserMarker();
      return;
    }

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

  const toggleUsingUserLocation = () => {
    if (location.coordinates.lat === '') return;

    setIsUsingUserLocation((prev) => !prev);
    createUserMarkerWithZoomingMap();
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

    watchPositionId.current = navigator.geolocation.watchPosition(
      onSuccess,
      onError,
    );
  };

  useEffect(() => {
    toggleUsingUserLocation();
  }, [location]);

  return {
    location,
    isUsingUserLocation,
    requestUserLocation,
    toggleUsingUserLocation,
  };
};

export default useGeoLocation;
