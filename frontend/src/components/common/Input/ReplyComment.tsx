import SingleComment from './SingleComment';

function ReplyComment({ commentList, pageTotalCommentList, depth }: any) {
  if (depth === 2) return null;
  return (
    <>
      {commentList.length > 0 &&
        commentList.map((comment: string) => (
          <>
            <SingleComment
              comment={comment}
              commentList={commentList}
              totalList={pageTotalCommentList}
              depth={depth}
            />
          </>
        ))}
    </>
  );
}

export default ReplyComment;
