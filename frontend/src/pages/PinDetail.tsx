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
import { putApi } from '../utils/putApi';
import Textarea from '../components/common/Textarea';
import Input from '../components/common/Input';
import { useSearchParams } from 'react-router-dom';

const PinDetail = ({ pinId }: { pinId: string }) => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [pin, setPin] = useState<PinType | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [formValues, setFormValues] = useState({
    name: '',
    address: '',
    description: '',
  });
  useEffect(() => {
    const getPinData = async () => {
      const pinData = await getApi(`/pin/${pinId}`);
      console.log('pinData', pinData);
      setPin(pinData);
      setFormValues({
        name: pinData.name,
        address: pinData.address,
        description: pinData.description,
      });
    };

    getPinData();
  }, [pinId, searchParams]);

  const updateQueryString = (key: string, value: string) => {
    setSearchParams({ ...Object.fromEntries(searchParams), [key]: value });
  };

  const removeQueryString = (key: string) => {
    const updatedSearchParams = { ...Object.fromEntries(searchParams) };
    delete updatedSearchParams[key];
    setSearchParams(updatedSearchParams);
  };

  const handleEditClick = () => {
    setIsEditing(true);
    updateQueryString('edit', 'true');
  };

  const handleUpdateClick = async () => {
    await putApi(`/pins/${pinId}`, formValues);
    setIsEditing(false);
    removeQueryString('edit');
  };

  const handleCancelClick = () => {
    setIsEditing(false);
  };

  const handleInputChange = (
    e: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>,
  ) => {
    const { name, value } = e.target;
    setFormValues((prevValues) => ({
      ...prevValues,
      [name]: value,
    }));
  };

  const copyContent = async () => {
    try {
      await navigator.clipboard.writeText(window.location.href);
    } catch (err) {
      if (typeof err === 'string') throw new Error(err);
      throw new Error('[ERROR] clipboard error');
    }
  };

  if (!pin) return <></>;

  return (
    <>
      <Flex $justifyContent="space-between" $alignItems="baseline" width="100%">
        <Text color="black" $fontSize="extraLarge" $fontWeight="normal">
          {isEditing ? (
            <Input
              type="text"
              name="name"
              value={formValues.name}
              onChange={handleInputChange}
            />
          ) : (
            pin.name
          )}
        </Text>
        {isEditing ? (
          <Flex>
            <Text
              color="primary"
              $fontSize="default"
              $fontWeight="normal"
              onClick={handleUpdateClick}
            >
              저장
            </Text>
            <Text
              color=""
              $fontSize="default"
              $fontWeight="normal"
              onClick={handleCancelClick}
            >
              취소
            </Text>
          </Flex>
        ) : (
          <Text
            color="primary"
            $fontSize="default"
            $fontWeight="normal"
            onClick={handleEditClick}
          >
            수정하기
          </Text>
        )}
      </Flex>
      <Space size={0} />

      <Flex $justifyContent="space-between" $alignItems="center" width="100%">
        <Text color="black" $fontSize="small" $fontWeight="normal">
          핀을 만든 사람
        </Text>
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          {pin.updatedAt}
        </Text>
      </Flex>
      <Space size={2} />
      <Flex
        width="100%"
        height="180px"
        $backgroundColor="gray"
        onClick={() => console.log('image')}
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
        {isEditing ? (
          <Input
            type="text"
            name="address"
            value={formValues.address}
            onChange={handleInputChange}
          />
        ) : (
          <Text color="gray" $fontSize="small" $fontWeight="normal">
            {pin.address}
          </Text>
        )}
      </Flex>
      <Space size={6} />
      <Flex $flexDirection="column">
        <Text color="black" $fontSize="medium" $fontWeight="bold">
          어떤 곳인가요?
        </Text>
        {isEditing ? (
          <Textarea
            name="description"
            value={formValues.description}
            onChange={handleInputChange}
          />
        ) : (
          <Text color="gray" $fontSize="small" $fontWeight="normal">
            {pin.description}
          </Text>
        )}
      </Flex>
      <Space size={7} />
      <Flex $justifyContent="center">
        <Clipping cursor={'pointer'} />
        <Space size={4} />
        <Share cursor={'pointer'} onClick={() => copyContent()} />
        <Space size={4} />
        <ShowDetail cursor={'pointer'} />
      </Flex>
    </>
  );
};

export default PinDetail;
