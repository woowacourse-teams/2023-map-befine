import { useState } from 'react';
import styled from 'styled-components';

import ReplyComment from './ReplyComment';
//  { 댓글, 댓글목록, 전체목록, depth = 0 }
function SingleComment({ comment, commentList, totalList, depth = 0 }: any) {
  const [replyOpen, setReplyOpen] = useState(false);
  const [seeMore, setSeeMore] = useState(false);
  const toggleReplyOpen = () => {
    setReplyOpen((prev) => !prev);
  };
  const toggleSeeMore = () => {
    setSeeMore((prev) => !prev);
  };

  const replyList = commentList.filter(
    (curComment: any) => curComment.replyTo === comment.id,
  );
  const replyCount = replyList.length;

  return (
    <CommentWrapper depth={depth} key={comment.id}>
      <Flex>
        <ProfileImage
          src={comment.writer.profileImage}
          width="40px"
          height="40px"
        />
        <CommentInfo>
          <Writer>@{comment.writer.name}</Writer>
          <Content>{comment.content}</Content>
          <div>
            <button type="button" onClick={toggleReplyOpen}>
              답글남기기
            </button>
            {replyOpen && (
              <form>
                <input />
              </form>
            )}
          </div>
          {replyCount > 0 && (
            <MoreReplyButton onClick={toggleSeeMore}>
              {seeMore ? '\u25B2' : '\u25BC'} 답글 {replyCount}개
            </MoreReplyButton>
          )}
        </CommentInfo>
      </Flex>

      {seeMore && (
        <ReplyComment
          commentList={replyList}
          parentId={comment.id}
          pageTotalCommentList={totalList}
          depth={depth + 1}
        />
      )}
    </CommentWrapper>
  );
}

export default SingleComment;

const CommentWrapper = styled.li<{ depth: number }>`
  width: 100%;
  margin-left: ${(props) => props.depth * 20}px;
`;

const Flex = styled.div`
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
`;

export const ProfileImage = styled.img`
  display: block;

  border-radius: 50%;
`;

const CommentInfo = styled.div``;

const Writer = styled.div`
  white-space: nowrap;
  font-size: 1rem;
  font-weight: 500;
  line-height: 1.8rem;
`;

const Content = styled.div`
  font-family: 'Roboto', 'Arial', sans-serif;
  font-size: 1.2rem;
  line-height: 1.6rem;
  font-weight: 300;
  line-height: 2rem;
  overflow-wrap: anywhere;
`;

const MoreReplyButton = styled.button`
  padding: 2px;
  border: none;
  background: none;
  border-radius: 8px;
  color: black;
  font-weight: 600;
  &:hover {
    background: gray;
    cursor: pointer;
  }
`;
