export interface EvCharger {
  evCharger: any[];
}

export interface NewAddress {
  centerLat: string;
  centerLon: string;
  frontLat: string;
}

export interface NewAddressList {
  newAddress: NewAddress[];
}

export interface Poi {
  id: string;
  pkey: string;
  navSeq: string;
  collectionType: string;
  name: string;

  adminDongCode: string;
  bizName: string;
  dataKind: string;
  desc: string;

  evChargers: EvCharger;
  firstBuildNo: string;
  firstNo: string;
  frontLat: string;
  frontLon: string;

  legalDongCode: string;

  lowerAddrName: string;

  middleAddrName: string;
  mlClass: string;

  newAddressList: NewAddressList;

  noorLat: string;
  noorLon: string;

  parkFlag: string;

  radius: string;

  roadName: string;

  rpFlag: string;

  secondBuildNo: string;

  secondNo: string;

  telNo: string;

  upperAddrName: string;

  zipCode: String;
}

export interface Pois {
  poi: Poi[];
}

export interface SearchPoiInfo {
  totalCount: string;
  count: string;
  page: string;
  pois: Pois;
}

export interface PoiApiResponse {
  searchPoiInfo: SearchPoiInfo;
}
