import { useEffect, useState } from 'react';

type GeoLocationState = {
  loaded: boolean;
  coordinates: { lat: string | number; lng: string | number };
  error?: { code: number; message: string };
};

const useGeoLocation = () => {
  const [location, setLocation] = useState<GeoLocationState>({
    loaded: false,
    coordinates: { lat: '', lng: '' },
  });

  const onSuccess = (location: any) => {
    setLocation({
      loaded: true,
      coordinates: {
        lat: location.coords.latitude,
        lng: location.coords.longitude,
      },
    });
  };

  const onError = (error: any) => {
    setLocation({
      loaded: true,
      coordinates: { lat: '', lng: '' },
      error: {
        code: error.code,
        message: error.message,
      },
    });
  };

  useEffect(() => {
    if (!('geolocation' in navigator)) {
      onError({ code: 0, message: 'Geolocation not supported' });
    }
    navigator.geolocation.getCurrentPosition(onSuccess, onError);
  }, []);

  return location;
};

export default useGeoLocation;
