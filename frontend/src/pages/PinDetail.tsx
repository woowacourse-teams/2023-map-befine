import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import { useEffect, useState } from 'react';
import { PinType } from '../types/Pin';
import { getApi } from '../apis/getApi';
import { useSearchParams } from 'react-router-dom';
import Box from '../components/common/Box';
import UpdatedPinDetail from './UpdatedPinDetail';
import useFormValues from '../hooks/useFormValues';
import { ModifyPinFormProps } from '../types/FormValues';
import useToast from '../hooks/useToast';
import Button from '../components/common/Button';
import ShareUrl from '../assets/shareUrl.svg';
import { styled } from 'styled-components';
import { ModalPortal, useModalContext } from '../context/AbsoluteModalContext';

interface PinDetailProps {
  pinId: number;
  isEditPinDetail: boolean;
  setIsEditPinDetail: React.Dispatch<React.SetStateAction<boolean>>;
}

const PinDetail = ({
  pinId,
  isEditPinDetail,
  setIsEditPinDetail,
}: PinDetailProps) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [pin, setPin] = useState<PinType | null>(null);
  const { showToast } = useToast();
  const { isModalOpen, openModal, closeModal } = useModalContext();
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

  useEffect(() => {
    const getPinData = async () => {
      const pinData = await getApi('default', `/pins/${pinId}`);
      setPin(pinData);
      setFormValues({
        name: pinData.name,
        images: pinData.image,
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
          핀 생성자
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
            {pin.updatedAt.split('T')[0].split('-').join('.')} 핀 수정자
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

      <Flex
        width="332px"
        height="48px"
        $justifyContent="center"
        position="fixed"
        bottom="24px"
      >
        <SaveToMyMapButton variant="primary" onClick={openModal}>
          내 지도에 저장하기
        </SaveToMyMapButton>
        <Space size={4} />
        <ShareButton variant="secondary" onClick={copyContent}>
          공유하기
        </ShareButton>
      </Flex>

      {isModalOpen && (
        <ModalPortal closeModalHandler={closeModal}>
          <>
            <Text
              color="black"
              $fontSize="extraLarge"
              $fontWeight="bold"
              $textAlign="center"
            >
              내 지도 목록
            </Text>
            <Space size={5} />
            <Flex $flexDirection="row">
              <Text
                color="black"
                $fontSize="small"
                $fontWeight="bold"
                $textAlign="center"
              >
                최신순
              </Text>
              <Space size={3} />
              <Text
                color="black"
                $fontSize="small"
                $fontWeight="bold"
                $textAlign="center"
              >
                글자순
              </Text>
              <Space size={3} />
              <Text
                color="black"
                $fontSize="small"
                $fontWeight="bold"
                $textAlign="center"
              >
                인기순
              </Text>
            </Flex>
            <Space size={2} />
            <Flex $flexDirection="column">
              <ModalMapItem>아이템</ModalMapItem>
              <Space size={4} />
              <ModalMapItem>아이템</ModalMapItem>
              <Space size={4} />
              <ModalMapItem>아이템</ModalMapItem>
              <Space size={4} />
              <ModalMapItem>아이템</ModalMapItem>
              <Space size={4} />
              <ModalMapItem>아이템</ModalMapItem>
              <Space size={4} />
            </Flex>

            <Flex $justifyContent="center">
              <SaveToMyMapButton variant="secondary" onClick={closeModal}>
                Close Modal
              </SaveToMyMapButton>
            </Flex>
          </>
        </ModalPortal>
      )}
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

const ModalMapItem = styled(Box)`
  width: 100%;
  height: 48px;

  text-align: center;

  border: 1px solid black;
  border-radius: 8px;
`;
export default PinDetail;
