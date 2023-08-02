import Flex from '../common/Flex';
import Text from '../common/Text';
import Clipping from '../../assets/clipping.svg';
import Share from '../../assets/share.svg';
import Button from '../common/Button';
import Space from '../common/Space';
import { useParams } from 'react-router-dom';
import useNavigator from '../../hooks/useNavigator';

export interface TopicInfoProps {
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
}: TopicInfoProps) => {
  const { topicId } = useParams();
  const { routePage } = useNavigator();

  const goToNewPin = () => {
    routePage(`/new-pin?topic-id=${topicId}`);
  };

  const copyContent = async () => {
    try {
      const topicUrl = window.location.href.split('?')[0];
      await navigator.clipboard.writeText(topicUrl);
      alert('핀의 링크가 복사되었습니다.');
    } catch (err) {
      if (typeof err === 'string') throw new Error(err);
      throw new Error('[ERROR] clipboard error');
    }
  };

  return (
    <Flex
      width="360px"
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
      <Space size={1} />
      <Text color="black" $fontSize="small" $fontWeight="normal">
        {topicOwner}
      </Text>
      <Space size={1} />
      <Text color="black" $fontSize="small" $fontWeight="normal">
        {topicDescription}
      </Text>
      <Space size={3} />
      <Flex $justifyContent="space-between">
        <Flex>
          <Clipping />
          <Space size={4} />
          <Share cursor="pointer" onClick={copyContent} />
        </Flex>
        <Button variant="primary" onClick={goToNewPin}>
          + 핀 추가하기
        </Button>
      </Flex>
      <Space size={6} />
    </Flex>
  );
};

export default TopicInfo;
