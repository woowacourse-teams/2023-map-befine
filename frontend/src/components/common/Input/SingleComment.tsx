/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
import { useState } from 'react';
import styled from 'styled-components';

import { deleteApi } from '../../../apis/deleteApi';
import { postApi } from '../../../apis/postApi';
import { putApi } from '../../../apis/putApi';
import useToast from '../../../hooks/useToast';
import { ConfirmCommentButton, CustomInput } from '../../../pages/PinDetail';
import Flex from '../Flex';
import Text from '../Text';
import ReplyComment from './ReplyComment';

const localStorageUser = localStorage?.getItem('user');
const user = JSON.parse(localStorageUser || '{}');
//  { 댓글, 댓글목록, 전체목록, depth = 0 }
function SingleComment({
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
      setIsEditing(false);
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
      <Flex $gap="12px">
        <ProfileImage
          src={comment.creatorImageUrl}
          width="40px"
          height="40px"
        />
        <CommentInfo>
          <Flex $justifyContent="space-between">
            <div>
              <Writer>
                <Text $fontSize="default" $fontWeight="bold" color="black">
                  @{comment.creator}
                </Text>
              </Writer>
            </div>
            {comment.canChange && (
              <Flex $gap="8px" cursor="pointer">
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
          <Content>
            {isEditing ? (
              <div
                style={{ width: '100%', display: 'flex', marginTop: '12px' }}
              >
                <CustomInput
                  value={content}
                  onChange={onContentChange}
                  placeholder="댓글 수정"
                />
                <ConfirmCommentButton
                  variant="secondary"
                  style={{ width: '60px', padding: '4px 12px' }}
                  // type="button"
                  onClick={onClickModifyConfirmBtn}
                >
                  등록
                </ConfirmCommentButton>
              </div>
            ) : (
              <Text $fontSize="default" $fontWeight="normal" color="darkGray">
                {comment.content}
              </Text>
            )}
          </Content>
          <div>
            {depth === 1 ? null : (
              <div
                onClick={toggleReplyOpen}
                style={{ cursor: 'pointer', marginBottom: '8px' }}
              >
                <Text color="black" $fontSize="small" $fontWeight="bold">
                  답글 작성
                </Text>
              </div>
            )}
            {replyOpen && (
              <div style={{ display: 'flex', gap: '12px' }}>
                <ProfileImage
                  src={user?.imageUrl || ''}
                  width="40px"
                  height="40px"
                />
                <div style={{ width: '100%' }}>
                  <CustomInput
                    value={newComment}
                    onChange={(e: any) => setNewComment(e.target.value)}
                    placeholder="댓글 추가"
                    // onClick={toggleReplyOpen}
                  />
                  <ConfirmCommentButton
                    variant="secondary"
                    style={{}}
                    // type="button"
                    onClick={onClickCommentBtn}
                  >
                    등록
                  </ConfirmCommentButton>
                </div>
              </div>
            )}
          </div>
          {replyCount > 0 && (
            <Flex>
              <ProfileImage
                src={replyList[0].creatorImageUrl || ''}
                width="28px"
                height="28px"
              />
              <MoreReplyButton onClick={toggleSeeMore}>
                {seeMore ? '\u25B2' : '\u25BC'} 답글 {replyCount}개
              </MoreReplyButton>
            </Flex>
          )}
        </CommentInfo>
      </Flex>

      {seeMore && (
        <ReplyComment
          commentList={replyList ?? []}
          parentId={comment.id}
          pageTotalCommentList={totalList}
          depth={depth + 1}
          refetch={refetch}
        />
      )}
    </CommentWrapper>
  );
}

export default SingleComment;

const CommentWrapper = styled.li<{ depth: number }>`
  margin-left: ${(props) => props.depth * 20}px;
  margin-top: 12px;
  list-style: none;
`;

export const ProfileImage = styled.img`
  display: block;

  border-radius: 50%;
`;

const CommentInfo = styled.div`
  flex: 1;
`;

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
