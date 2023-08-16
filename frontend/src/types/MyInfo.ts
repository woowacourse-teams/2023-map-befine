export interface MyInfoType {
  name: string;
  email: string;
}

export interface MyInfoTopicType {
  id: number;
  name: string;
  image: string;
  pinCount: number;
  bookmarkCount: number;
  isBookmarked: boolean;
  updatedAt: string;
}

export interface MyInfoPinType {
  id: number;
  name: string;
  address: string;
  description: string;
  latitude: number;
  longitude: number;
}
