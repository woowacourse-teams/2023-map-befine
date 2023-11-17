import { useEffect, useState } from 'react';
import { styled } from 'styled-components';

import SmallTopicPin from '../../assets/smallTopicPin.svg';
import SmallTopicStar from '../../assets/smallTopicStar.svg';
import FavoriteSVG from '../../assets/topicInfo_favoriteBtn_filled.svg';
import FavoriteNotFilledSVG from '../../assets/topicInfo_favoriteBtn_notFilled.svg';
import SeeTogetherSVG from '../../assets/topicInfo_seeTogetherBtn_filled.svg';
import SeeTogetherNotFilledSVG from '../../assets/topicInfo_seeTogetherBtn_notFilled.svg';
import TopicShareUrlSVG from '../../assets/topicInfo_shareUrl.svg';
import UpdateBtnSVG from '../../assets/updateBtn.svg';
import { DEFAULT_TOPIC_IMAGE } from '../../constants';
import useToast from '../../hooks/useToast';
import AddFavorite from '../AddFavorite';
import AddSeeTogether from '../AddSeeTogether';
import Box from '../common/Box';
import Flex from '../common/Flex';
import Image from '../common/Image';
import Space from '../common/Space';
import Text from '../common/Text';
import UpdatedTopicInfo from './UpdatedTopicInfo';

export interface TopicInfoProps {
  topicId: string;
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

function TopicInfo({
  topicId,
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
}: TopicInfoProps) {
  const [isUpdate, setIsUpdate] = useState<boolean>(false);
  const { showToast } = useToast();

  const updateTopicInfo = () => {
    setIsUpdate(true);
  };

  const copyContent = async () => {
    try {
      const topicUrl = window.location.href.split('?')[0];
      await navigator.clipboard.writeText(topicUrl);
      showToast('info', '지도 링크가 복사되었습니다.');
    } catch (err) {
      showToast('error', '지도 링크를 복사하는데 실패했습니다.');
    }
  };

  const onChangeIsInAtlas = () => {
    showToast('error', '비회원은 홈에서만 모아보기에 담을 수 있습니다.');
    return false;
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
        setTopicsFromServer={setTopicsFromServer}
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
        src={topicImage || DEFAULT_TOPIC_IMAGE}
        alt="사진 이미지"
        $objectFit="cover"
        radius="medium"
        $errorDefaultSrc={DEFAULT_TOPIC_IMAGE}
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
            <UpdateBtnSVG />
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
          parentType="topicInfo"
          isInAtlas={isInAtlas}
          onClickAtlas={onChangeIsInAtlas}
          id={Number(topicId)}
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
          id={Number(topicId)}
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
}

const ButtonsWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

const TopicImage = styled(Image)`
  border-radius: ${({ theme }) => theme.radius.medium};
`;

export default TopicInfo;
