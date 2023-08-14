import Flex from '../common/Flex';
import Text from '../common/Text';
import Image from '../common/Image';
import Button from '../common/Button';
import Space from '../common/Space';
import useNavigator from '../../hooks/useNavigator';
import useToast from '../../hooks/useToast';
import SmallTopicPin from '../../assets/smallTopicPin.svg';
import SmallTopicStar from '../../assets/smallTopicStar.svg';
import TopicShareUrlSVG from '../../assets/topicInfo_shareUrl.svg';
import TopicFavoriteSVG from '../../assets/topicInfo_favorite.svg';
import TopicSeeTogetherSVG from '../../assets/topicInfo_seeTogether.svg';
import { DEFAULT_TOPIC_IMAGE } from '../../constants';
import AddSeeTogether from '../AddSeeTogether';
import AddFavorite from '../AddFavorite';

const FAVORITE_COUNT = 10;

export interface TopicInfoProps {
  fullUrl?: string;
  topicId: number;
  topicImage: string;
  topicParticipant: number;
  topicPinCount: number;
  topicTitle: string;
  topicOwner: string;
  topicDescription: string;
}

const TopicInfo = ({
  fullUrl,
  topicId,
  topicImage,
  topicParticipant,
  topicPinCount,
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
      position="relative"
      $flexDirection="column"
      $backgroundColor="white"
      tabIndex={0}
      role="button"
    >
      <Image
        height="168px"
        width="332px"
        src={topicImage}
        alt="토픽 이미지"
        $objectFit="cover"
        onError={(e: React.SyntheticEvent<HTMLImageElement, Event>) => {
          e.currentTarget.src = DEFAULT_TOPIC_IMAGE;
        }}
      />

      <Space size={1} />

      <Flex>
        <Flex $alignItems="center" width="76px">
          <Text color="black" $fontSize="small" $fontWeight="normal">
            👨‍💻 {FAVORITE_COUNT > 999 ? '+999' : FAVORITE_COUNT}명
          </Text>
        </Flex>
        <Flex $alignItems="center" width="72px">
          <SmallTopicPin />
          <Space size={0} />
          <Text color="black" $fontSize="small" $fontWeight="normal">
            {topicPinCount > 999 ? '+999' : topicPinCount}개
          </Text>
        </Flex>
        <Flex $alignItems="center" width="72px">
          <SmallTopicStar />
          <Space size={0} />
          <Text color="black" $fontSize="small" $fontWeight="normal">
            {FAVORITE_COUNT > 999 ? '+999' : FAVORITE_COUNT}명
          </Text>
        </Flex>
      </Flex>

      <Space size={1} />

      <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
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

      <Flex $justifyContent="center">
        <AddSeeTogether id={topicId}>
          <TopicSeeTogetherSVG />
        </AddSeeTogether>
        <Space size={5} />
        <AddFavorite id={topicId}>
          <TopicFavoriteSVG />
        </AddFavorite>
        <Space size={5} />
        <TopicShareUrlSVG cursor="pointer" onClick={copyContent} />
      </Flex>

      <Space size={3} />
    </Flex>
  );
};

export default TopicInfo;
