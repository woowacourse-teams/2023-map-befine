import Flex from '../common/Flex';
import Text from '../common/Text';
import Clipping from '../../assets/clipping.svg';
import Share from '../../assets/Share2.svg';
import Button from '../common/Button';
import Space from '../common/Space';
import useNavigator from '../../hooks/useNavigator';
import useToast from '../../hooks/useToast';
import Back from '../../assets/Back.svg';
import Favorite from '../../assets/Favorite.svg';
import Star from '../../assets/Star2.svg';

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
  const { showToast } = useToast();

  const goToNewPin = () => {
    routePage(`/new-pin?topic-id=${topicId}`, fullUrl);
  };

  const copyContent = async () => {
    try {
      const topicUrl = window.location.href.split('?')[0];
      await navigator.clipboard.writeText(topicUrl);
      showToast('info', '토픽 링크가 복사되었습니다.');
    } catch (err) {
      showToast('error', '토픽 링크를 복사하는데 실패했습니다.');
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
      <Flex padding={2} $alignItems="center" $justifyContent="space-between">
        <Back onClick={() => routePage('/')} />
        {/* <Space size={7} /> */}
        <Text color="primary" $fontSize="large" $fontWeight="bold">
          {topicTitle}
        </Text>
        <Share cursor="pointer" onClick={copyContent} />
      </Flex>

      <Flex height="200px" $gap="4px">
        <Flex style={{ height: '200px', width: '160px' }}>
          <img
            height="200px"
            width="160px"
            src="https://cutewallpaper.org/24/free-sun-pictures/140668415.jpg"
          />
        </Flex>

        <Flex $flexDirection="column" width="100%">
          <Flex height="60%" overflow="hidden" $flexDirection="column">
            <Text color="black" $fontSize="small" $fontWeight="bold">
              생성자 : {topicOwner}
            </Text>
            <Space size={1} />
            <Text color="gray" $fontSize="small" $fontWeight="normal">
              {topicDescription}
            </Text>
          </Flex>
          <Space size={6} />

          <Flex $justifyContent="space-between" $alignItems="center">
            <Flex $flexDirection="column">
              <Text color="gray" $fontSize="small" $fontWeight="normal">
                장소 :{pinNumber}
              </Text>
              <Text color="gray" $fontSize="small" $fontWeight="normal">
                즐겨찾기 : {pinNumber}
              </Text>
            </Flex>
            <Star />
          </Flex>
        </Flex>
      </Flex>

      <Space size={3} />
      {/* <Flex $justifyContent="space-between">
        <Flex>
          <Clipping />
          <Space size={4} />
          <Share cursor="pointer" onClick={copyContent} />
        </Flex>
        <Button variant="primary" onClick={goToNewPin}>
          + 핀 추가하기
        </Button>
      </Flex>
      <Space size={6} /> */}
    </Flex>
  );
};

export default TopicInfo;
