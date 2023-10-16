import { useState } from 'react';
import styled from 'styled-components';

import { postApi } from '../../../apis/postApi';
import useToast from '../../../hooks/useToast';
import ReplyComment from './ReplyComment';

const userToken = localStorage?.getItem('userToken');
const localStorageUser = localStorage?.getItem('user');
const user = JSON.parse(localStorageUser || '{}');
//  { 댓글, 댓글목록, 전체목록, depth = 0 }
function SingleComment({
  pinId,
  comment,
  commentList,
  totalList,
  depth = 0,
}: any) {
  const [replyOpen, setReplyOpen] = useState(false);
  const [seeMore, setSeeMore] = useState(false);
  const [newComment, setNewComment] = useState<string>('');
  const { showToast } = useToast();

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

  const onClickCommentBtn = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();

    try {
      // 댓글 추가
      // comment 값이랑 추가 정보 body에 담아서 보내기
      await postApi(
        `/pins/comments`,
        {
          pinId,
          content: newComment,
          parentPinCommentId: null,
        },
        'application/json',
      );

      setCurrentPageCommentList();

      showToast('info', '댓글이 추가되었습니다.');
    } catch {
      showToast('error', '댓글을 다시 작성해주세요');
    }
  };
  return (
    <CommentWrapper depth={depth} key={comment.id}>
      <Flex>
        <ProfileImage
          src={comment.creatorImageUrl}
          width="40px"
          height="40px"
        />
        <CommentInfo>
          <Writer>@{comment.creator}</Writer>
          <Content>{comment.content}</Content>
          <div>
            <button type="button" onClick={toggleReplyOpen}>
              답글남기기
            </button>
            {replyOpen && (
              <div
                style={{ display: 'flex', marginBottom: '20px', gap: '12px' }}
              >
                <ProfileImage
                  src={user?.imageUrl || ''}
                  width="40px"
                  height="40px"
                />
                <div style={{ width: '100%' }}>
                  <input
                    style={{
                      width: '100%',
                      borderTop: 'none',
                      borderLeft: 'none',
                      borderRight: 'none',
                      fontSize: '16px',
                    }}
                    value={newComment}
                    onChange={(e: any) => setNewComment(e.target.value)}
                    placeholder="댓글 추가"
                    // onClick={toggleReplyOpen}
                  />
                  <button
                    style={{
                      marginTop: '12px',
                      float: 'right',
                      width: '40px',
                      fontSize: '12px',
                    }}
                    type="button"
                    onClick={onClickCommentBtn}
                  >
                    등록
                  </button>

                  {/* {replyOpen && (
            <form>
              <input />
            </form>
          )} */}
                </div>
              </div>
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
