import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Text from '../common/Text';
import Clipping from '../../assets/clipping.svg';
import Share from '../../assets/share.svg';
import Button from '../common/Button';
import Space from '../common/Space';
import { useNavigate, useParams } from 'react-router-dom';

export interface TopicCardProps {
  topicParticipant: number;
  pinNumber: number;
  topicTitle: string;
  topicOwner: string;
  topicDescription: string;
}

const TopicInfo = ({
  topicParticipant,
  pinNumber,
  topicTitle,
  topicOwner,
  topicDescription,
}: TopicCardProps) => {
  const navigator = useNavigate();
  const { topicId } = useParams();

  const goToNewPin = () => {
    navigator(`/new-pin?topic-id=${topicId}`);
  };

  return (
    <Flex
      width="360px"
      height="195px"
      position="relative"
      $flexDirection="column"
      $backgroundColor="white"
      $borderBottom="1px solid #E7E7E7"
    >
      <Flex>
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          · {topicParticipant}명의 참가자
        </Text>
        <Space size={2} />
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          · {pinNumber}개의 핀
        </Text>
      </Flex>
      <Text color="black" $fontSize="large" $fontWeight="bold">
        {topicTitle}
      </Text>
      <Text color="black" $fontSize="small" $fontWeight="normal">
        {topicOwner}
      </Text>
      <Text color="gray" $fontSize="small" $fontWeight="normal">
        {topicDescription}
      </Text>
      <Space size={2} />
      <Flex $justifyContent="space-between">
        <Flex>
          <Clipping />
          <Space size={2} />
          <Share />
        </Flex>
        <Button variant="secondary" onClick={goToNewPin}>
          + 핀 추가하기
        </Button>
      </Flex>
      <Space size={6} />
    </Flex>
  );
};

export default TopicInfo;
