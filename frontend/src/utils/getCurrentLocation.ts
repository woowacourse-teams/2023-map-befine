const getCurrentLocation = () => {
  const onSuccess = (pos: GeolocationPosition) => {
    const latitude = pos.coords.latitude;
    const longitude = pos.coords.longitude;
  };

  const onFail = () => {
    alert('현재 위치를 가져올 수 없어 기본 위치로 지정됩니다.');
  };

  navigator.geolocation.getCurrentPosition(onSuccess, onFail);
};
