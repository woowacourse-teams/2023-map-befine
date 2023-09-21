import { getMapApi } from '../apis/getMapApi';
import { MapAddressProps } from '../types/Map';

const getAddressFromServer = async (lat: number, lng: number) => {
  const version = '1';
  const coordType = 'WGS84GEO';
  const addressType = 'A10';
  const callback = 'result';

  const addressData = await getMapApi<MapAddressProps>(
    `https://apis.openapi.sk.com/tmap/geo/reversegeocoding?version=${version}&lat=${lat}&lon=${lng}&coordType=${coordType}&addressType=${addressType}&callback=${callback}&appKey=P2MX6F1aaf428AbAyahIl9L8GsIlES04aXS9hgxo
      `,
  );

  const addressResult = addressData.addressInfo.fullAddress.split(',');
  return addressResult[2];
};

export default getAddressFromServer;
