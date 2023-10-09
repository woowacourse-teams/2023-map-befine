export const getPoiApi = async <T>(query: string) => {
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

  const responseData: T = await response.json();

  const { searchPoiInfo } = responseData;
  return searchPoiInfo;
};
