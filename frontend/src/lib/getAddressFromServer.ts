import { getMapApi } from '../apis/getMapApi';

const getAddressFromServer = async (lat: any, lng: any) => {
  const version = '1';
  const coordType = 'WGS84GEO';
  const addressType = 'A10';
  const callback = 'result';
  const addressData = await getMapApi(
    `https://apis.openapi.sk.com/tmap/geo/reversegeocoding?version=${version}&lat=${lat}&lon=${lng}&coordType=${coordType}&addressType=${addressType}&callback=${callback}&appKey=P2MX6F1aaf428AbAyahIl9L8GsIlES04aXS9hgxo
      `,
  );

  if (addressData.error) {
    return addressData.error;
  }

  const addressResult = addressData.addressInfo.fullAddress.split(',');
  return addressResult[2];
};

export default getAddressFromServer;
