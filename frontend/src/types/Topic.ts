import { PinType } from './Pin';

export interface TopicType {
  id: number;
  name: string;
  image: string;
  pinCount: number;
  updatedAt: string;
}

export interface TopicInfoType {
  id: number;
  name: string;
  description: string;
  pinCount: number;
  updatedAt: string;
  pins: PinType[];
}
