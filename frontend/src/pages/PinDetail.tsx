import { useContext, useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { styled } from 'styled-components';

import { getApi } from '../apis/getApi';
import { postApi } from '../apis/postApi';
import UpdateBtnSVG from '../assets/updateBtn.svg';
import Box from '../components/common/Box';
import Button from '../components/common/Button';
import Flex from '../components/common/Flex';
import SingleComment, {
  ProfileImage,
} from '../components/common/Input/SingleComment';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import Modal from '../components/Modal';
import AddToMyTopicList from '../components/ModalMyTopicList/addToMyTopicList';
import PinImageContainer from '../components/PinImageContainer';
import { ModalContext } from '../context/ModalContext';
import useCompressImage from '../hooks/useCompressImage';
import useFormValues from '../hooks/useFormValues';
import useToast from '../hooks/useToast';
import { ModifyPinFormProps } from '../types/FormValues';
import { PinProps } from '../types/Pin';
import UpdatedPinDetail from './UpdatedPinDetail';

interface PinDetailProps {
  width: '372px' | '100vw';
  pinId: number;
  isEditPinDetail: boolean;
  setIsEditPinDetail: React.Dispatch<React.SetStateAction<boolean>>;
}

const userToken = localStorage?.getItem('userToken');
const localStorageUser = localStorage?.getItem('user');
const user = JSON.parse(localStorageUser);

function PinDetail({
  width,
  pinId,
  isEditPinDetail,
  setIsEditPinDetail,
}: PinDetailProps) {
  console.log(user);

  const [searchParams, setSearchParams] = useSearchParams();
  const [pin, setPin] = useState<PinProps | null>(null);
  const [commentList, setCommentList] = useState<any[]>([]); // 댓글 리스트
  const [newComment, setNewComment] = useState<string>('');
  const { showToast } = useToast();
  const {
    formValues,
    errorMessages,
    setFormValues,
    setErrorMessages,
    onChangeInput,
  } = useFormValues<ModifyPinFormProps>({
    name: '',
    description: '',
  });
  const { openModal } = useContext(ModalContext);
  const { compressImage } = useCompressImage();

  const openModalWithToken = () => {
    if (userToken) {
      openModal('addToMyTopicList');
      return;
    }

    showToast('error', '로그인 후 사용해주세요.');
  };

  const getPinData = async () => {
    const pinData = await getApi<PinProps>(`/pins/${pinId}`);
    setPin(pinData);
    setFormValues({
      name: pinData.name,
      description: pinData.description,
    });
  };

  useEffect(() => {
    getPinData();
  }, [pinId, searchParams]);

  const onClickEditPin = () => {
    setIsEditPinDetail(true);
    setErrorMessages({
      name: '',
      description: '',
    });
  };

  const copyContent = async () => {
    try {
      await navigator.clipboard.writeText(window.location.href);
      showToast('info', '핀 링크가 복사되었습니다.');
    } catch (err) {
      showToast('error', '핀 링크를 복사하는데 실패했습니다.');
    }
  };

  const onPinImageFileChange = async (
    event: React.ChangeEvent<HTMLInputElement>,
  ) => {
    const file = event.target.files && event.target.files[0];
    const formData = new FormData();

    if (!file) {
      showToast(
        'error',
        '이미지를 선택하지 않았거나 추가하신 이미지를 찾을 수 없습니다. 다시 선택해 주세요.',
      );
      return;
    }

    const compressedFile = await compressImage(file);

    formData.append('image', compressedFile);

    const data = JSON.stringify(pinId);
    const jsonBlob = new Blob([data], { type: 'application/json' });

    formData.append('pinId', jsonBlob);

    await postApi('/pins/images', formData);

    getPinData();
  };

  // 댓글 구현 부분
  const setCurrentPageCommentList = async () => {
    const data: any[] = await getApi(`/pins/comments/${pinId}`);
    setCommentList(data);
    return data;
  };

  useEffect(() => {
    setCurrentPageCommentList();
  }, []);

  const onClickCommentBtn = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();

    try {
      // 댓글 추가
      // comment 값이랑 추가 정보 body에 담아서 보내기
      await postApi(
        `/pins/comment`,
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

  if (!pin) return <></>;

  if (isEditPinDetail)
    return (
      <Wrapper $layoutWidth={width} $selectedPinId={pinId}>
        <UpdatedPinDetail
          searchParams={searchParams}
          setSearchParams={setSearchParams}
          setIsEditing={setIsEditPinDetail}
          updatePinDetailAfterEditing={getPinData}
          pinId={pinId}
          formValues={formValues}
          errorMessages={errorMessages}
          onChangeInput={onChangeInput}
        />
      </Wrapper>
    );

  return (
    <Wrapper $layoutWidth={width} $selectedPinId={pinId} data-cy="pin-detail">
      <Flex $justifyContent="space-between" $alignItems="baseline" width="100%">
        <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
          {pin.name}
        </Text>
      </Flex>
      <Space size={1} />
      <Flex $justifyContent="space-between" $alignItems="flex-end" width="100%">
        <Text color="black" $fontSize="small" $fontWeight="normal">
          {pin.creator}
        </Text>
        <Flex $flexDirection="column" $alignItems="flex-end">
          {pin.canUpdate ? (
            <Box cursor="pointer">
              <UpdateBtnSVG onClick={onClickEditPin} />
            </Box>
          ) : (
            <Space size={5} />
          )}
          <Text color="black" $fontSize="small" $fontWeight="normal">
            {pin.updatedAt.split('T')[0].split('-').join('.')}
          </Text>
        </Flex>
      </Flex>
      <Space size={2} />
      <ImageInputLabel htmlFor="file">파일업로드</ImageInputLabel>
      <ImageInputButton
        id="file"
        type="file"
        name="image"
        onChange={onPinImageFileChange}
      />
      <PinImageContainer images={pin.images} getPinData={getPinData} />
      <Space size={6} />
      <Flex $flexDirection="column">
        <Text color="black" $fontSize="large" $fontWeight="bold">
          어디에 있나요?
        </Text>
        <Space size={1} />
        <Text color="black" $fontSize="default" $fontWeight="normal">
          {pin.address}
        </Text>
      </Flex>
      <Space size={6} />
      <Flex $flexDirection="column">
        <Text color="black" $fontSize="large" $fontWeight="bold">
          어떤 곳인가요?
        </Text>
        <Space size={1} />
        <Text color="black" $fontSize="default" $fontWeight="normal">
          {pin.description}
        </Text>
      </Flex>
      <Space size={7} />
      {/*  Comment Section */}

      <Text color="black" $fontSize="large" $fontWeight="bold">
        댓글{' '}
      </Text>
      <Space size={1} />
      {userToken && (
        <div style={{ display: 'flex', marginBottom: '20px', gap: '12px' }}>
          <ProfileImage src={user?.imageUrl || ''} width="40px" height="40px" />
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
      {commentList?.length > 0 &&
        commentList.map(
          (comment: any) =>
            !comment.replyTo ? (
              <SingleComment
                key={comment.id}
                comment={comment}
                commentList={commentList}
                totalList={commentList}
              />
            ) : null, // <-- comment.replyTo가 존재하는 경우 null 반환
        )}
      {/* comment section END */}
      <ButtonsWrapper>
        <SaveToMyMapButton variant="primary" onClick={openModalWithToken}>
          내 지도에 저장하기
        </SaveToMyMapButton>
        <Space size={3} />
        <ShareButton variant="secondary" onClick={copyContent}>
          공유하기
        </ShareButton>
      </ButtonsWrapper>
      <Space size={8} />
      <Modal
        modalKey="addToMyTopicList"
        position="center"
        width="744px"
        height="512px"
        $dimmedColor="rgba(0,0,0,0.25)"
      >
        <ModalContentsWrapper>
          <Space size={5} />
          <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
            내 토픽 리스트
          </Text>
          <Text color="gray" $fontSize="small" $fontWeight="normal">
            핀을 저장할 지도를 선택해주세요.
          </Text>
          <Space size={4} />
          <AddToMyTopicList pin={pin} />
        </ModalContentsWrapper>
      </Modal>
    </Wrapper>
  );
}

const Wrapper = styled.section<{
  $layoutWidth: '372px' | '100vw';
  $selectedPinId: number | null;
}>`
  display: flex;
  flex-direction: column;
  width: ${({ $layoutWidth }) => $layoutWidth};
  height: calc(var(--vh, 1vh) * 100);
  overflow: auto;
  position: absolute;
  top: 0;
  left: ${({ $layoutWidth }) => $layoutWidth};
  padding: ${({ theme }) => theme.spacing[4]};
  border-left: 1px solid ${({ theme }) => theme.color.gray};
  background-color: ${({ theme }) => theme.color.white};
  z-index: 1;

  @media (max-width: 1076px) {
    width: 50vw;
    margin-top: calc(var(--vh, 1vh) * 50);
    height: ${({ $layoutWidth }) =>
      $layoutWidth === '372px' && 'calc(var(--vh, 1vh) * 50)'};
    left: ${({ $selectedPinId }) => $selectedPinId && '50vw'};
  }

  @media (max-width: 744px) {
    border-left: 0;
    left: 0;
    width: 100vw;
  }

  @media (max-width: 372px) {
    width: ${({ $layoutWidth }) => $layoutWidth};
  }
`;

const SaveToMyMapButton = styled(Button)`
  font-size: ${({ theme }) => theme.fontSize.default};
  font-weight: ${({ theme }) => theme.fontWeight.bold};

  box-shadow: 8px 8px 8px 0px rgba(69, 69, 69, 0.15);
`;

const ShareButton = styled(Button)`
  font-size: ${({ theme }) => theme.fontSize.default};
  font-weight: ${({ theme }) => theme.fontWeight.bold};

  box-shadow: 8px 8px 8px 0px rgba(69, 69, 69, 0.15);
`;

const ModalContentsWrapper = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: white;
  overflow: scroll;
`;

const ButtonsWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 332px;
  height: 48px;
  margin: 0 auto;
`;

const ImageInputLabel = styled.label`
  width: 80px;
  height: 40px;
  margin-bottom: 10px;
  padding: 10px 10px;

  color: ${({ theme }) => theme.color.black};
  background-color: ${({ theme }) => theme.color.lightGray};

  font-size: ${({ theme }) => theme.fontSize.extraSmall};
  text-align: center;

  cursor: pointer;
`;

const ImageInputButton = styled.input`
  display: none;
`;

export default PinDetail;
