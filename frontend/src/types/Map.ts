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

export interface MapAddressProps {
  addressInfo: AddressInfoProps;
}
