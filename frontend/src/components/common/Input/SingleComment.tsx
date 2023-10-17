/* eslint-disable jsx-a11y/click-events-have-key-events */
import { useState } from 'react';
import styled from 'styled-components';

import { deleteApi } from '../../../apis/deleteApi';
import { postApi } from '../../../apis/postApi';
import { putApi } from '../../../apis/putApi';
import useToast from '../../../hooks/useToast';
import Text from '../Text';
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
  refetch,
}: any) {
  const [replyOpen, setReplyOpen] = useState(false);
  const [seeMore, setSeeMore] = useState(false);
  const [newComment, setNewComment] = useState<string>('');
  const { showToast } = useToast();
  const [isEditing, setIsEditing] = useState(false);
  const [content, setContent] = useState(comment.content);
  const params = new URLSearchParams(window.location.search);
  const pinDetail = params.get('pinDetail');

  const toggleReplyOpen = () => {
    setReplyOpen((prev) => !prev);
  };
  const toggleSeeMore = () => {
    setSeeMore((prev) => !prev);
  };

  const replyList = commentList?.filter(
    (curComment: any) => curComment.parentPinCommentId === comment.id,
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
          pinId: Number(pinDetail),
          content: newComment,
          parentPinCommentId: comment.id,
        },
        'application/json',
      );
      await refetch(Number(pinDetail));
      setReplyOpen(false);
      setNewComment('');
      showToast('info', '댓글이 추가되었습니다.');
    } catch (e) {
      showToast('error', '댓글을 다시 작성해주세요');
    }
  };

  const onClickDeleteBtn = async (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();
    try {
      // 댓글 삭제
      await deleteApi(`/pins/comments/${comment.id}`);
      await refetch(Number(pinDetail));
      showToast('info', '댓글이 삭제되었습니다.');
    } catch (e) {
      console.error(e);
      showToast('error', '댓글을 다시 작성해주세요');
    }
  };

  const onContentChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setContent(e.target.value);
  };

  const onClickModifyConfirmBtn = async (
    e: React.MouseEvent<HTMLButtonElement>,
  ) => {
    e.stopPropagation();
    try {
      // 댓글 수정
      await putApi(`/pins/comments/${comment.id}`, {
        content,
      });
      await refetch(Number(pinDetail));
      setIsEditing;
      showToast('info', '댓글이 수정되었습니다.');
    } catch (e) {
      console.error(e);
      showToast('error', '댓글을 다시 작성해주세요');
    }
  };

  const onClickModifyBtn = () => {
    setIsEditing((prev) => !prev);
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
          <Writer>
            <Text $fontSize="default" $fontWeight="bold" color="black">
              @{comment.creator}
            </Text>
          </Writer>
          <Content>
            {isEditing ? (
              <Flex>
                <input value={content} onChange={onContentChange} />
                <button type="button" onClick={onClickModifyConfirmBtn}>
                  확인
                </button>
              </Flex>
            ) : (
              <Text $fontSize="large" $fontWeight="normal" color="darkGray">
                {comment.content}
              </Text>
            )}
          </Content>
          <div>
            {depth === 1 ? null : (
              <button type="button" onClick={toggleReplyOpen}>
                답글남기기
              </button>
            )}
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
        {comment.canChange && (
          <Flex>
            <Text
              $fontSize="small"
              color="gray"
              $fontWeight="bold"
              onClick={onClickModifyBtn}
            >
              수정
            </Text>
            <Text
              $fontSize="small"
              color="primary"
              $fontWeight="bold"
              onClick={onClickDeleteBtn}
            >
              삭제
            </Text>
          </Flex>
        )}
      </Flex>

      {seeMore && (
        <ReplyComment
          commentList={replyList ?? []}
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
  list-style: none;
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
`;

const Content = styled.div`
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
