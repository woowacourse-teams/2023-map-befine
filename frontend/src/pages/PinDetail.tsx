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
import { ModifyPinFormProps } from '../types/FormValues';
import useToast from '../hooks/useToast';
import Button from '../components/common/Button';
import Modal from '../components/Modal';
import { styled } from 'styled-components';
import { ModalContext } from '../context/ModalContext';
import AddToMyTopicList from '../components/ModalMyTopicList/addToMyTopicList';

interface PinDetailProps {
  width: '372px' | '100vw';
  topicId: string;
  pinId: number;
  isEditPinDetail: boolean;
  setIsEditPinDetail: React.Dispatch<React.SetStateAction<boolean>>;
}

const userToken = localStorage.getItem('userToken');

const PinDetail = ({
  width,
  topicId,
  pinId,
  isEditPinDetail,
  setIsEditPinDetail,
}: PinDetailProps) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [pin, setPin] = useState<PinProps | null>(null);
  const [selectedTopic, setSelectedTopic] = useState<any>(null); //토픽이 없을 때 사용하는 변수
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

  const getPinData = async () => {
    const pinData = await getApi<PinProps>(`/pins/${pinId}`);
    setPin(pinData);
    setFormValues({
      name: pinData.name,
      images: pinData.images,
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
      images: '',
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
    <Wrapper $layoutWidth={width} $selectedPinId={pinId}>
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

      <Space size={7} />

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
};

const Wrapper = styled.section<{
  $layoutWidth: '372px' | '100vw';
  $selectedPinId: number | null;
}>`
  display: flex;
  flex-direction: column;
  width: ${({ $layoutWidth }) => $layoutWidth};
  height: 100vh;
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
    margin-top: 50vh;
    height: ${({ $layoutWidth }) => $layoutWidth === '372px' && '50vh'};
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
  margin: 0 auto;
`;

export default PinDetail;
