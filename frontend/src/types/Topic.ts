import { PinType } from './Pin';

export interface TopicType {
  id: number;
  name: string;
  image: string;
  creator: string;
  pinCount: number;
  bookmarkCount: number;
  updatedAt: string;
  isInAtlas: false;
  isBookmarked: false;
}

export interface TopicDetailType {
  id: number;
  name: string;
  creator: string;
  description: string;
  pinCount: number;
  bookmarkCount: number;
  updatedAt: string;
  isInAtlas: false;
  isBookmarked: false;
  pins: PinType[];
}
