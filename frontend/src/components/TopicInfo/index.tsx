import Flex from '../common/Flex';
import Text from '../common/Text';
import Image from '../common/Image';
import Space from '../common/Space';
import useNavigator from '../../hooks/useNavigator';
import useToast from '../../hooks/useToast';
import SmallTopicPin from '../../assets/smallTopicPin.svg';
import SmallTopicStar from '../../assets/smallTopicStar.svg';
import TopicShareUrlSVG from '../../assets/topicInfo_shareUrl.svg';
import FavoriteNotFilledSVG from '../../assets/topicInfo_favoriteBtn_notFilled.svg';
import SeeTogetherNotFilledSVG from '../../assets/topicInfo_seeTogetherBtn_notFilled.svg';
import SeeTogetherSVG from '../../assets/topicInfo_seeTogetherBtn_filled.svg';
import { DEFAULT_TOPIC_IMAGE } from '../../constants';
import AddSeeTogether from '../AddSeeTogether';
import AddFavorite from '../AddFavorite';

export interface TopicInfoProps {
  fullUrl?: string;
  topicId: string;
  topicImage: string;
  topicTitle: string;
  topicCreator: string;
  topicUpdatedAt: string;
  topicPinCount: number;
  topicBookmarkCount: number;
  topicDescription: string;
  isInAtlas: boolean;
  isBookmarked: boolean;
  setTopicsFromServer: () => void;
}

const TopicInfo = ({
  fullUrl,
  topicId,
  topicImage,
  topicTitle,
  topicCreator,
  topicUpdatedAt,
  topicPinCount,
  topicBookmarkCount,
  topicDescription,
  isInAtlas,
  isBookmarked,
  setTopicsFromServer,
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
            {topicBookmarkCount > 999 ? '+999' : topicBookmarkCount}명
          </Text>
        </Flex>
      </Flex>

      <Space size={0} />

      <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
        {topicTitle}
      </Text>
      <Space size={1} />
      <Text color="black" $fontSize="small" $fontWeight="normal">
        {topicCreator}
      </Text>
      <Space size={1} />
      <Text color="black" $fontSize="small" $fontWeight="normal">
        {topicDescription}
      </Text>
      <Space size={3} />
      <Text color="gray" $fontSize="small" $fontWeight="normal">
        {topicUpdatedAt.split('T')[0].replaceAll('-', '.')} 업데이트
      </Text>

      <Space size={3} />

      <Flex $justifyContent="center">
        <AddSeeTogether
          isInAtlas={isInAtlas}
          id={Number(topicId)}
          setTopicsFromServer={setTopicsFromServer}
        >
          {isInAtlas ? (
            <SeeTogetherSVG width="40px" height="40px" />
          ) : (
            <SeeTogetherNotFilledSVG />
          )}
        </AddSeeTogether>
        <Space size={5} />
        <AddFavorite id={Number(topicId)}>
          <FavoriteNotFilledSVG />
        </AddFavorite>
        <Space size={5} />
        <TopicShareUrlSVG cursor="pointer" onClick={copyContent} />
      </Flex>

      <Space size={3} />
    </Flex>
  );
};

export default TopicInfo;
