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
  canUpdate: boolean;
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

export interface TopicAuthorInfo {
  publicity: 'PUBLIC' | 'PRIVATE';
  permissionMembers: TopicAuthorMemberWithId[];
}

export interface TopicAuthorMemberWithId {
  id: number;
  memberResponse: TopicAuthorMember;
}

export interface TopicAuthorMember {
  id: number;
  nickName: string;
  email: string;
}
