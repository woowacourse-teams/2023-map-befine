import Flex from '../common/Flex';
import Text from '../common/Text';
import Clipping from '../../assets/clipping.svg';
import Share from '../../assets/share.svg';
import Button from '../common/Button';
import Space from '../common/Space';
import useNavigator from '../../hooks/useNavigator';

export interface TopicInfoProps {
  fullUrl?: string;
  topicId?: string;
  topicParticipant: number;
  pinNumber: number;
  topicTitle: string;
  topicOwner: string;
  topicDescription: string;
}

const TopicInfo = ({
  fullUrl,
  topicId,
  topicParticipant,
  pinNumber,
  topicTitle,
  topicOwner,
  topicDescription,
}: TopicInfoProps) => {
  const { routePage } = useNavigator();

  const goToNewPin = () => {
    routePage(`/new-pin?topic-id=${topicId}`, fullUrl);
  };

  const copyContent = async () => {
    try {
      const topicUrl = window.location.href.split('?')[0];
      await navigator.clipboard.writeText(topicUrl);
      alert('토픽 링크가 복사되었습니다.');
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
      tabIndex={0}
      role="button"
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
