export interface MemberProps {
  id: number;
  nickName: string;
  email: string;
  imageUrl: string;
  updatedAt: string;
}

export interface LoginResponseProps {
  accessToken: string;
  member: MemberProps;
}
