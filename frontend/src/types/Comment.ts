interface Comment {
  id: number;
  content: string;
  creator: string;
  creatorImageUrl: string;
  parentPinCommentId: number | null;
  canChange: boolean;
  updatedAt: String;
}
