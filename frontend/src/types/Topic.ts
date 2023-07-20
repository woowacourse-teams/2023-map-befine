import { PinType } from './Pin';

export interface TopicType {
  id: string;
  name: string;
  emoji: string;
  description: string;
  pinCount: number;
  updatedAt: string;
}

export interface TopicInfoType {
  id: string;
  name: string;
  description: string;
  pinCount: number;
  updatedAt: string;
  pins: PinType[];
}
