export interface MapAddressProps {
  addressInfo: AddressInfoProps;
}

export interface AddressInfoProps {
  addressType: string;
  adminDong: string;
  adminDongCode: string;
  buildingIndex: string;
  buildingName: string;
  bunji: string;
  city_do: string;
  eup_myun: string;
  fullAddress: string;
  gu_gun: string;
  legalDong: string;
  legalDongCode: string;
  mappingDistance: string;
  ri: string;
  roadCode: string;
  roadName: string;
}

export interface MapProps {
  isMobile: boolean;
  mouseClickFlag: boolean;
  name: string;
  _data: MapDataProps;
  _object_: MapObjectProps;
  _status: MapStatusProps;
}

export interface MapDataProps {
  mapType: number;
  maxBounds: {};
  target: string;
  container: {};
  vsmMap: {};
  vsmOptions: {};
  minZoomLimit: number;
  maxZoomLimit: number;
  options: MapOptionsProps;
}

export interface MapObjectProps {
  eventListeners: {};
  getHandlers: string;
  fireEvent: string;
}

export interface MapStatusProps {
  zoom: number;
  center: {};
  width: number;
  height: number;
}

export interface MapOptionsProps {
  draggable: boolean;
  measureControl: boolean;
  naviControl: boolean;
  pinchZoom: boolean;
  scaleBar: boolean;
  scrollwheel: boolean;
}
