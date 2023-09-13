import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import { useContext, useEffect, useState } from 'react';
import { PinProps } from '../types/Pin';
import { getApi } from '../apis/getApi';
import { useSearchParams } from 'react-router-dom';
import Box from '../components/common/Box';
import UpdatedPinDetail from './UpdatedPinDetail';
import useFormValues from '../hooks/useFormValues';
import { ModifyPinFormProps } from '../types/tmap';
import useToast from '../hooks/useToast';
import Button from '../components/common/Button';
import Modal from '../components/Modal';
import { styled } from 'styled-components';
import { ModalContext } from '../context/ModalContext';
import AddToMyTopicList from '../components/ModalMyTopicList/addToMyTopicList';

interface PinDetailProps {
  topicId: string;
  pinId: number;
  isEditPinDetail: boolean;
  setIsEditPinDetail: React.Dispatch<React.SetStateAction<boolean>>;
}

const userToken = localStorage.getItem('userToken');

const PinDetail = ({
  pinId,
  isEditPinDetail,
  setIsEditPinDetail,
}: PinDetailProps) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [pin, setPin] = useState<PinProps | null>(null);
  const { showToast } = useToast();
  const {
    formValues,
    errorMessages,
    setFormValues,
    setErrorMessages,
    onChangeInput,
  } = useFormValues<ModifyPinFormProps>({
    name: '',
    images: [],
    description: '',
  });
  const { openModal } = useContext(ModalContext);

  const openModalWithToken = () => {
    if (userToken) {
      openModal('addToMyTopicList');
      return;
    }

    showToast('error', '로그인 후 사용해주세요.');
  };

  useEffect(() => {
    const getPinData = async () => {
      const pinData = await getApi<PinProps>(`/pins/${pinId}`);
      setPin(pinData);
      setFormValues({
        name: pinData.name,
        images: pinData.images,
        description: pinData.description,
      });
    };

    getPinData();
  }, [pinId, searchParams]);

  const updateQueryString = (key: string, value: string) => {
    setSearchParams({ ...Object.fromEntries(searchParams), [key]: value });
  };

  const onClickEditPin = () => {
    setIsEditPinDetail(true);
    setErrorMessages({
      name: '',
      images: '',
      description: '',
    });
    updateQueryString('edit', 'true');
  };

  const copyContent = async () => {
    try {
      await navigator.clipboard.writeText(window.location.href);
      showToast('info', '핀 링크가 복사되었습니다.');
    } catch (err) {
      showToast('error', '핀 링크를 복사하는데 실패했습니다.');
    }
  };

  if (!pin) return <></>;

  if (isEditPinDetail)
    return (
      <UpdatedPinDetail
        searchParams={searchParams}
        setSearchParams={setSearchParams}
        setIsEditing={setIsEditPinDetail}
        pinId={pinId}
        formValues={formValues}
        errorMessages={errorMessages}
        onChangeInput={onChangeInput}
      />
    );

  return (
    <>
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
          <Box cursor="pointer">
            <Text
              color="primary"
              $fontSize="default"
              $fontWeight="normal"
              onClick={onClickEditPin}
            >
              수정하기
            </Text>
          </Box>
          <Text color="black" $fontSize="small" $fontWeight="normal">
            {pin.updatedAt.split('T')[0].split('-').join('.')}
          </Text>
        </Flex>
      </Flex>

      <Space size={2} />

      <PinDetailImgContainer
        width="100%"
        height="180px"
        $backgroundColor="whiteGray"
        $alignItems="center"
        $justifyContent="center"
        $flexDirection="column"
        padding={7}
        $borderRadius="small"
      >
        <Text
          color="darkGray"
          $fontSize="default"
          $fontWeight="normal"
          $textAlign="center"
        >
          + 사진을 추가해주시면 더 알찬 정보를 제공해줄 수 있을 것 같아요.
        </Text>
      </PinDetailImgContainer>

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

      <ButtonsWrapper>
        <SaveToMyMapButton variant="primary" onClick={openModalWithToken}>
          내 지도에 저장하기
        </SaveToMyMapButton>
        <Space size={4} />
        <ShareButton variant="secondary" onClick={copyContent}>
          공유하기
        </ShareButton>
      </ButtonsWrapper>

      <Modal
        modalKey="addToMyTopicList"
        position="center"
        width="768px"
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
    </>
  );
};

const PinDetailImgContainer = styled(Flex)`
  box-shadow: 8px 8px 8px 0px rgba(69, 69, 69, 0.15);
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
  position: fixed;
  bottom: 24px;
`;

export default PinDetail;
