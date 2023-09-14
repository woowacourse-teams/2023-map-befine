import { PinProps } from './Pin';

export interface TopicCardProps {
  id: number;
  name: string;
  image: string;
  creator: string;
  pinCount: number;
  bookmarkCount: number;
  updatedAt: string;
  isInAtlas: boolean;
  isBookmarked: boolean;
}

export interface TopicDetailProps {
  id: number;
  image: string;
  name: string;
  creator: string;
  description: string;
  pinCount: number;
  bookmarkCount: number;
  updatedAt: string;
  isInAtlas: boolean;
  isBookmarked: boolean;
  pins: PinProps[];
}

export interface ModalTopicCardProps {
  id: number;
  name: string;
  creator: string;
  image: string;
  pinCount: number;
  bookmarkCount: number;
  isBookmarked: boolean;
  updatedAt: string;
}

export interface TopicAuthor {
  publicity: 'PUBLIC' | 'PRIVATE';
  permissionMembers: PermissionMember[];
}

interface PermissionMember {
  id: number;
  memberResponse: Member;
}

export interface Member {
  id: number;
  nickName: string;
  email: string;
}
