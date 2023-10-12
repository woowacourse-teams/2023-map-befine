import { PoiApiResponse } from '../types/Poi';

export const getPoiApi = async (query: string): Promise<PoiApiResponse> => {
  const response = await fetch(
    `https://apis.openapi.sk.com/tmap/pois?version=1&format=json&callback=result&searchKeyword=${query}&resCoordType=WGS84GEO&reqCoordType=WGS84GEO&count=10`,
    {
      method: 'GET',
      headers: { appKey: process.env.TMAP_API_KEY || '' },
    },
  );

  if (response.status >= 400) {
    throw new Error('[POI] GET 요청에 실패했습니다.');
  }

  const responseData = await response.json();

  return responseData;
};
