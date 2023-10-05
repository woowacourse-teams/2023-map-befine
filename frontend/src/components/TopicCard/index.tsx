import { styled } from 'styled-components';
import Text from '../common/Text';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';
import Image from '../common/Image';
import { SyntheticEvent, useContext, useState } from 'react';
import Space from '../common/Space';
import Flex from '../common/Flex';
import FavoriteSVG from '../../assets/favoriteBtn_filled.svg';
import FavoriteNotFilledSVG from '../../assets/favoriteBtn_notFilled.svg';
import SeeTogetherSVG from '../../assets/seeTogetherBtn_filled.svg';
import SeeTogetherNotFilledSVG from '../../assets/seeTogetherBtn_notFilled.svg';
import SmallTopicPin from '../../assets/smallTopicPin.svg';
import SmallTopicStar from '../../assets/smallTopicStar.svg';
import { DEFAULT_TOPIC_IMAGE } from '../../constants';
import AddSeeTogether from '../AddSeeTogether';
import AddFavorite from '../AddFavorite';
import { TopicCardProps } from '../../types/Topic';
import useKeyDown from '../../hooks/useKeyDown';
import { ModalContext } from '../../context/ModalContext';
import useToast from '../../hooks/useToast';

interface OnClickDesignatedProps {
  topicId: number;
  topicName: string;
}

interface TopicCardExtendedProps extends TopicCardProps {
  cardType: 'default' | 'modal';
  onClickDesignated?: ({ topicId, topicName }: OnClickDesignatedProps) => void;
  getTopicsFromServer?: () => void;
}

const TopicCard = ({
  cardType,
  id,
  image,
  creator,
  name,
  updatedAt,
  pinCount,
  bookmarkCount,
  isInAtlas,
  isBookmarked,
  onClickDesignated,
  getTopicsFromServer,
}: TopicCardExtendedProps) => {
  const { routePage } = useNavigator();
  const { closeModal } = useContext(ModalContext);
  const { elementRef, onElementKeyDown } = useKeyDown<HTMLLIElement>();
  const [isInNonMemberAtlas, setIsInNonMemberAtlas] =
    useState<boolean>(isInAtlas);
  const { showToast } = useToast();

  const goToSelectedTopic = () => {
    routePage(`/topics/${id}`, [id]);
  };

  const addPinToThisTopic = () => {
    if (onClickDesignated) {
      onClickDesignated({ topicId: id, topicName: name });
    }

    closeModal('newPin');
  };

  const onClickIsInAtlas = () => {
    setIsInNonMemberAtlas(!isInNonMemberAtlas);
    if (!isInNonMemberAtlas) {
      showToast('info', '모아보기에 추가했습니다.');
      return;
    }
    showToast('info', '해당 지도를 모아보기에서 제외했습니다.');
  };
  return (
    <Wrapper
      data-cy="topic-card"
      onClick={cardType === 'default' ? goToSelectedTopic : addPinToThisTopic}
      ref={elementRef}
      onKeyDown={onElementKeyDown}
    >
      <Flex position="relative" tabIndex={0} role="button">
        <TopicImage
          height="138px"
          width="138px"
          src={image}
          alt="사진 이미지"
          $objectFit="cover"
          onError={(e: SyntheticEvent<HTMLImageElement, Event>) => {
            e.currentTarget.src = DEFAULT_TOPIC_IMAGE;
          }}
        />

        <Box width="192px" padding={1}>
          <Box height="52px">
            <Text
              color="black"
              $fontSize="default"
              $fontWeight="bold"
              aria-label={`토픽 이름 ${name}`}
            >
              {name}
            </Text>
          </Box>

          <Text
            color="black"
            $fontSize="small"
            $fontWeight="normal"
            aria-label={`작성자 ${creator}`}
          >
            {creator}
          </Text>

          <Space size={0} />

          <Text color="gray" $fontSize="small" $fontWeight="normal">
            {updatedAt.split('T')[0].replaceAll('-', '.')} 업데이트
          </Text>

          <Space size={0} />

          <Flex>
            <Flex $alignItems="center" width="64px">
              <SmallTopicPin />
              <Space size={0} />
              <Text
                color="black"
                $fontSize="extraSmall"
                $fontWeight="normal"
                aria-label={`핀 갯수 ${pinCount}개`}
              >
                {pinCount > 999 ? '+999' : pinCount}개
              </Text>
            </Flex>
            <Flex $alignItems="center" width="64px">
              <SmallTopicStar />
              <Space size={0} />
              <Text
                color="black"
                $fontSize="extraSmall"
                $fontWeight="normal"
                aria-label={`즐겨찾기 ${bookmarkCount}명`}
              >
                {bookmarkCount > 999 ? '+999' : bookmarkCount}명
              </Text>
            </Flex>
          </Flex>

          {cardType === 'default' && getTopicsFromServer && (
            <ButtonWrapper>
              <AddSeeTogether
                isInAtlas={isInAtlas}
                onClickAtlas={onClickIsInAtlas}
                id={id}
                getTopicsFromServer={getTopicsFromServer}
              >
                {isInNonMemberAtlas ? (
                  <SeeTogetherSVG />
                ) : (
                  <SeeTogetherNotFilledSVG />
                )}
              </AddSeeTogether>
              <AddFavorite
                isBookmarked={isBookmarked}
                id={id}
                getTopicsFromServer={getTopicsFromServer}
              >
                {isBookmarked ? <FavoriteSVG /> : <FavoriteNotFilledSVG />}
              </AddFavorite>
            </ButtonWrapper>
          )}
        </Box>
      </Flex>
    </Wrapper>
  );
};

const Wrapper = styled.li`
  width: 332px;
  height: 140px;
  cursor: pointer;
  border: 1px solid ${({ theme }) => theme.color.gray};
  border-radius: ${({ theme }) => theme.radius.small};
`;

const ButtonWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  position: absolute;
  width: 72px;

  top: 100px;
  left: 60px;
`;

const TopicImage = styled(Image)`
  border-radius: ${({ theme }) => theme.radius.small};
`;

export default TopicCard;
