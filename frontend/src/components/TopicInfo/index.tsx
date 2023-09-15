import Flex from '../common/Flex';
import Text from '../common/Text';
import Image from '../common/Image';
import Space from '../common/Space';
import useToast from '../../hooks/useToast';
import SmallTopicPin from '../../assets/smallTopicPin.svg';
import SmallTopicStar from '../../assets/smallTopicStar.svg';
import TopicShareUrlSVG from '../../assets/topicInfo_shareUrl.svg';
import FavoriteSVG from '../../assets/topicInfo_favoriteBtn_filled.svg';
import FavoriteNotFilledSVG from '../../assets/topicInfo_favoriteBtn_notFilled.svg';
import SeeTogetherNotFilledSVG from '../../assets/topicInfo_seeTogetherBtn_notFilled.svg';
import SeeTogetherSVG from '../../assets/topicInfo_seeTogetherBtn_filled.svg';
import { DEFAULT_TOPIC_IMAGE } from '../../constants';
import AddSeeTogether from '../AddSeeTogether';
import AddFavorite from '../AddFavorite';
import { styled } from 'styled-components';
import Box from '../common/Box';
import { useEffect, useState } from 'react';
import UpdatedTopicInfo from './UpdatedTopicInfo';

export interface TopicInfoProps {
  topicId: string;
  idx: number;
  topicImage: string;
  topicTitle: string;
  topicCreator: string;
  topicUpdatedAt: string;
  topicPinCount: number;
  topicBookmarkCount: number;
  topicDescription: string;
  canUpdate: boolean;
  isInAtlas: boolean;
  isBookmarked: boolean;
  setTopicsFromServer: () => void;
}

const TopicInfo = ({
  topicId,
  idx,
  topicImage,
  topicTitle,
  topicCreator,
  topicUpdatedAt,
  topicPinCount,
  topicBookmarkCount,
  topicDescription,
  canUpdate,
  isInAtlas,
  isBookmarked,
  setTopicsFromServer,
}: TopicInfoProps) => {
  const [isUpdate, setIsUpdate] = useState<boolean>(false);
  const { showToast } = useToast();

  const updateTopicInfo = () => {
    setIsUpdate(true);
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

  useEffect(() => {
    if (!isUpdate) setTopicsFromServer();
  }, [isUpdate]);

  if (isUpdate) {
    return (
      <UpdatedTopicInfo
        id={Number(topicId)}
        image={topicImage}
        name={topicTitle}
        description={topicDescription}
        setIsUpdate={setIsUpdate}
      />
    );
  }

  return (
    <Flex
      position="relative"
      $flexDirection="column"
      $backgroundColor="white"
      tabIndex={0}
      role="button"
      data-cy="topic-info"
    >
      <TopicImage
        height="168px"
        width="100%"
        src={topicImage}
        alt="토픽 이미지"
        $objectFit="cover"
        onError={(e: React.SyntheticEvent<HTMLImageElement, Event>) => {
          e.currentTarget.src = DEFAULT_TOPIC_IMAGE;
        }}
      />

      <Space size={1} />

      <Flex $justifyContent="space-between">
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
        {canUpdate && (
          <Box cursor="pointer" onClick={updateTopicInfo}>
            <Text color="primary" $fontSize="default" $fontWeight="normal">
              수정하기
            </Text>
          </Box>
        )}
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

      <ButtonsWrapper>
        <AddSeeTogether
          isInAtlas={isInAtlas}
          id={Number(topicId.split(',')[idx])}
          getTopicsFromServer={setTopicsFromServer}
        >
          {isInAtlas ? (
            <SeeTogetherSVG width="40px" height="40px" />
          ) : (
            <SeeTogetherNotFilledSVG />
          )}
        </AddSeeTogether>
        <Space size={5} />
        <AddFavorite
          isBookmarked={isBookmarked}
          id={Number(topicId.split(',')[idx])}
          getTopicsFromServer={setTopicsFromServer}
        >
          {isBookmarked ? <FavoriteSVG /> : <FavoriteNotFilledSVG />}
        </AddFavorite>
        <Space size={5} />
        <TopicShareUrlSVG cursor="pointer" onClick={copyContent} />
      </ButtonsWrapper>

      <Space size={3} />
    </Flex>
  );
};

const ButtonsWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

const TopicImage = styled(Image)`
  border-radius: ${({ theme }) => theme.radius.medium};
`;

export default TopicInfo;
