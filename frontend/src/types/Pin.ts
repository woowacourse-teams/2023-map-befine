export interface PinProps {
  id: number;
  name: string;
  creator: string;
  address: string;
  description: string;
  latitude: number;
  longitude: number;
  canUpdate: boolean;
  updatedAt: string;
  images: string[];
}
