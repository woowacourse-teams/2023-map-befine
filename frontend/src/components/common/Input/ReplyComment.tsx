import SingleComment from './SingleComment';

interface ReplyCommentProps {
  commentList: Comment[];
  pageTotalCommentList: Comment[];
  depth: number;
  refetch: (pinId: number) => Promise<Comment[]>;
}

function ReplyComment({
  commentList,
  pageTotalCommentList,
  depth,
  refetch,
}: ReplyCommentProps) {
  if (depth === 2) return null;
  return (
    <>
      {commentList.length > 0 &&
        commentList.map((comment) => (
          <>
            <SingleComment
              comment={comment}
              commentList={commentList}
              totalList={pageTotalCommentList}
              depth={depth}
              refetch={refetch}
            />
          </>
        ))}
    </>
  );
}

export default ReplyComment;
