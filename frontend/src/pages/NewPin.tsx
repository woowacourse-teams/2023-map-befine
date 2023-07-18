import Box from '../components/common/Box';
import Input from '../components/common/Input';
import Text from '../components/common/Text';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import Button from '../components/common/Button';
import Textarea from '../components/common/Textarea';

interface NewPinProps {
  topicName: string;
}

const NewPin = ({ topicName }: NewPinProps) => {
  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();
  };

  return (
    <form onSubmit={onSubmit}>
      <Space size={6} />
      <Flex $flexDirection="column">
        <Text color="black" $fontSize="large" $fontWeight="bold">
          핀 추가
        </Text>

        <Space size={5} />

        <Box>
          <Flex>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              토픽 선택
            </Text>
            <Space size={0} />
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </Flex>
          <Space size={0} />
          <Button variant="primary">{topicName}</Button>
        </Box>

        <Space size={5} />

        <Box>
          <Flex>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              장소 이름
            </Text>
            <Space size={0} />
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </Flex>
          <Space size={0} />
          <Input placeholder="지도를 클릭하거나 장소의 이름을 입력해주세요." />
        </Box>

        <Space size={5} />

        <Box>
          <Flex>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              장소 위치
            </Text>
            <Space size={0} />
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </Flex>
          <Space size={0} />
          <Input placeholder="지도를 클릭하거나 장소의 위치를 입력해주세요." />
        </Box>

        <Space size={5} />

        <Box>
          <Flex>
            <Text color="black" $fontSize="default" $fontWeight="normal">
              장소 설명
            </Text>
            <Space size={0} />
            <Text color="primary" $fontSize="extraSmall" $fontWeight="normal">
              *
            </Text>
          </Flex>
          <Space size={0} />
          <Textarea placeholder="장소에 대한 의견을 자유롭게 남겨주세요." />
        </Box>

        <Space size={6} />

        <Flex $justifyContent="end">
          <Button variant="primary">추가하기</Button>
          <Space size={3} />
          <Button variant="secondary">취소하기</Button>
        </Flex>
      </Flex>
    </form>
  );
};

export default NewPin;
