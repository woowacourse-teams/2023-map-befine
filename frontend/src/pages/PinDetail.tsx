import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import Plus from '../assets/plus.svg';
import Clipping from '../assets/clipping.svg';
import ShowDetail from '../assets/showDetail.svg';
import Share from '../assets/share.svg';
import { useEffect, useState } from 'react';
import { PinType } from '../types/Pin';
import { getApi } from '../utils/getApi';
import { useSearchParams } from 'react-router-dom';
import Box from '../components/common/Box';
import UpdatedPinDetail from './UpdatedPinDetail';
import useFormValues from '../hooks/useFormValues';
import { DefaultPinValuesType } from '../types/FormValues';

const PinDetail = ({ pinId }: { pinId: number }) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [pin, setPin] = useState<PinType | null>(null);
  const [isEditing, setIsEditing] = useState<boolean>(false);
  const { formValues, setFormValues, onChangeInput } =
    useFormValues<DefaultPinValuesType>({
      id: 0,
      name: '',
      images: [],
      description: '',
      address: '',
      latitude: '',
      longitude: '',
      updatedAt: '',
    });

  useEffect(() => {
    const getPinData = async () => {
      const pinData = await getApi(`/pins/${pinId}`);
      setPin(pinData);
      setFormValues({
        id: pinData.id,
        name: pinData.name,
        images: pinData.images,
        description: pinData.description,
        address: pinData.address,
        latitude: pinData.latitude,
        longitude: pinData.longitude,
        updatedAt: pinData.updatedAt,
      });
    };

    getPinData();
  }, [pinId, searchParams]);

  const updateQueryString = (key: string, value: string) => {
    setSearchParams({ ...Object.fromEntries(searchParams), [key]: value });
  };

  const onClickEditPin = () => {
    setIsEditing(true);
    updateQueryString('edit', 'true');
  };

  const copyContent = async () => {
    try {
      await navigator.clipboard.writeText(window.location.href);
      alert('핀의 링크가 복사되었습니다.');
    } catch (err) {
      if (typeof err === 'string') throw new Error(err);
      throw new Error('[ERROR] clipboard error');
    }
  };

  if (!pin) return <></>;

  if (isEditing)
    return (
      <UpdatedPinDetail
        searchParams={searchParams}
        setSearchParams={setSearchParams}
        setIsEditing={setIsEditing}
        pinId={pinId}
        formValues={formValues}
        onChangeInput={onChangeInput}
      />
    );

  return (
    <>
      <Flex $justifyContent="space-between" $alignItems="baseline" width="100%">
        <Text color="black" $fontSize="extraLarge" $fontWeight="normal">
          {pin.name}
        </Text>
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
      </Flex>
      <Space size={0} />

      <Flex $justifyContent="space-between" $alignItems="center" width="100%">
        <Text color="black" $fontSize="small" $fontWeight="normal">
          핀을 만든 사람
        </Text>
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          {`${pin.updatedAt.split('T')[0].split('-').join('.')} 업데이트`}
        </Text>
      </Flex>
      <Space size={2} />
      <Flex
        width="100%"
        height="180px"
        $backgroundColor="gray"
        $alignItems="center"
        $justifyContent="center"
        $flexDirection="column"
        padding={7}
        $borderRadius="small"
      >
        <Plus />
        <Space size={1} />
        <Text
          color="white"
          $fontSize="default"
          $fontWeight="normal"
          $textAlign="center"
        >
          사진을 추가해주시면 더 알찬 정보를 제공해줄 수 있을 것 같아요.
        </Text>
      </Flex>
      <Space size={6} />
      <Flex $flexDirection="column">
        <Text color="black" $fontSize="medium" $fontWeight="bold">
          어디에 있나요?
        </Text>
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          {pin.address}
        </Text>
      </Flex>
      <Space size={6} />
      <Flex $flexDirection="column">
        <Text color="black" $fontSize="medium" $fontWeight="bold">
          어떤 곳인가요?
        </Text>
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          {pin.description}
        </Text>
      </Flex>
      <Space size={7} />
      <Flex $justifyContent="center">
        <Clipping cursor={'pointer'} />
        <Space size={4} />
        <Share cursor={'pointer'} onClick={copyContent} />
        <Space size={4} />
        <ShowDetail cursor={'pointer'} />
      </Flex>
    </>
  );
};

export default PinDetail;
