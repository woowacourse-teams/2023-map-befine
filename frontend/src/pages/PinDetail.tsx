import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Text from '../components/common/Text';

import Plus from '../assets/plus.svg';
import Clipping from '../assets/clipping.svg';
import ShowDetail from '../assets/showDetail.svg';
import Share from '../assets/share.svg';
import { useEffect, useState } from 'react';

const PinDetail = ({ pinId }: any) => {
  const [pin, setPin] = useState<any>({});
  useEffect(() => {
    fetch(`/pin/${pinId}`)
      .then((res) => res.json())
      .then((data) => setPin(data));
    console.log('fetch pin detail', pin);
  }, [pinId]);

  const copyContent = async () => {
    try {
      await navigator.clipboard.writeText(window.location.href);
      console.log('Content copied to clipboard');
    } catch (err) {
      console.error('Failed to copy: ', err);
    }
  };

  return (
    <>
      <Flex $justifyContent="space-between" $alignItems="baseline" width="100%">
        <Text color="black" $fontSize="extraLarge" $fontWeight="normal">
          오또상스시
        </Text>
        <Text color="primary" $fontSize="default" $fontWeight="normal">
          글 수정하기
        </Text>
      </Flex>
      <Space size={0} />

      <Flex $justifyContent="space-between" $alignItems="center" width="100%">
        <Text color="black" $fontSize="small" $fontWeight="normal">
          매튜의 이야기
        </Text>
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          2021.09.28
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
          서울특별시 선릉 테헤란로 192-46
        </Text>
      </Flex>
      <Space size={6} />
      <Flex $flexDirection="column">
        <Text color="black" $fontSize="medium" $fontWeight="bold">
          어떤 곳인가요?
        </Text>
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          초밥을 파는 곳입니다. 점심 특선 있고 초밥 질이 괜찮습니다. 가격대도
          다른 곳에 비해서 양호한 편이고 적당히 생각날 때 가면 좋을 것 같습니다.
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
