import { forwardRef, useContext, useEffect, useRef } from 'react';
import Flex from './common/Flex';
import { MarkerContext } from '../context/MarkerContext';
import { getAddress } from '../utils/getAddress';
import useMapClick from '../hooks/useMapClick';
import useClickedCoordinate from '../hooks/useClickedCoordinate';
import useUpdateCoordinates from '../hooks/useUpdateCoordinates';

const Map = (props: any, ref: any) => {
  const { map } = props;
  const { markers } = useContext(MarkerContext);
  const bounds = useRef(new window.Tmapv2.LatLngBounds());

  const getAddressFromServer = async (lat: any, lng: any) => {
    const version = '1';
    const coordType = 'WGS84GEO';
    const addressType = 'A10';
    const callback = 'result';
    const addressData = await getAddress(
      `https://apis.openapi.sk.com/tmap/geo/reversegeocoding?version=${version}&lat=${lat}&lon=${lng}&coordType=${coordType}&addressType=${addressType}&callback=${callback}&appKey=P2MX6F1aaf428AbAyahIl9L8GsIlES04aXS9hgxo
      `,
    );
    const addressResult = addressData.addressInfo.fullAddress.split(',');
    return addressResult[2];
  };
  useMapClick({ map, getAddressFromServer });
  useClickedCoordinate(map);
  useUpdateCoordinates(map);

  useEffect(() => {
    // 홈 화면이 아니고 마커가 하나만 있으면 해당 마커로 지도의 중심을 이동

    if (markers.length === 1) {
      map.panTo(markers[0].getPosition());
    }

    // 마커들을 모두 포함하는 경계를 계산
    if (markers.length > 1) {
      bounds.current = new window.Tmapv2.LatLngBounds();
      markers.forEach((marker: any) => {
        bounds.current.extend(marker.getPosition());
      });
      map.fitBounds(bounds.current);
    }
  }, [markers]);

  // url 파라미터에 pinDetail이 있으면 해당 pin으로 지도의 중심을 이동
  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    if (queryParams.has('pinDetail')) {
      const pinId = queryParams.get('pinDetail');

      const marker = markers.find((marker: any) => marker.id === pinId);
      if (marker) {
        map.panTo(marker.getPosition());
        //marker 객체의 animation 속성을 변경
        marker._marker_data.options.animation =
          window.Tmapv2.MarkerOptions.ANIMATE_BALLOON;
        marker._marker_data.options.animationLength = 500;
      }
    }
  }, [markers]);

  return <Flex flex="1" id="map" ref={ref} height="100vh" width="100%" />;
};

export default forwardRef(Map);
