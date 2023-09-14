export interface PinProps {
  id: number;
  name: string;
  creator: string;
  address: string;
  description: string;
  latitude: number;
  longitude: number;
  updatedAt: string;
  images: ImagesType[];
}

export interface ImagesType {
  id: number;
  imageUrl: string;
}