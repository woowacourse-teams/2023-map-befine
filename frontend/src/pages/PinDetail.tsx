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

const PinDetail = ({ pinId }: { pinId: string }) => {
  const [pin, setPin] = useState<PinType | null>(null);

  useEffect(() => {
    const getPinData = async () => {
      const pinData = await getApi(`/pin/${pinId}`);
      setPin(pinData);
    };

    getPinData();
  }, [pinId]);

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
          {pin.name}
        </Text>
        <Text color="primary" $fontSize="default" $fontWeight="normal">
          글 수정하기
        </Text>
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
        <Share cursor={'pointer'} onClick={() => copyContent()} />
        <Space size={4} />
        <ShowDetail cursor={'pointer'} />
      </Flex>
    </>
  );
};

export default PinDetail;
