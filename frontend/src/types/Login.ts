export interface Member {
  id: number;
  nickName: string;
  email: string;
  imageUrl: string;
  updatedAt: string;
}

export interface LoginResponse {
  accessToken: string;
  member: Member;
}
