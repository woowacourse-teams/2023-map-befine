import { getApi } from '../apis/getApi';

const getAddressFromServer = async (lat: any, lng: any) => {
  const version = '1';
  const coordType = 'WGS84GEO';
  const addressType = 'A10';
  const callback = 'result';
  const addressData = await getApi(
    'tMap',
    `https://apis.openapi.sk.com/tmap/geo/reversegeocoding?version=${version}&lat=${lat}&lon=${lng}&coordType=${coordType}&addressType=${addressType}&callback=${callback}&appKey=P2MX6F1aaf428AbAyahIl9L8GsIlES04aXS9hgxo
      `,
  );
  const addressResult = addressData.addressInfo.fullAddress.split(',');
  return addressResult[2];
};

export default getAddressFromServer;
