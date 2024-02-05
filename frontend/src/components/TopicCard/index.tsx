import { QueryObserverResult, RefetchOptions } from '@tanstack/react-query';
import { useContext, useState } from 'react';
import { styled } from 'styled-components';

import SeeTogetherSVG from '../../assets/seeTogetherBtn_filled.svg';
import SeeTogetherNotFilledSVG from '../../assets/seeTogetherBtn_notFilled.svg';
import SmallTopicPin from '../../assets/smallTopicPin.svg';
import SmallTopicStar from '../../assets/smallTopicStar.svg';
import { DEFAULT_TOPIC_IMAGE } from '../../constants';
import { ModalContext } from '../../context/ModalContext';
import useKeyDown from '../../hooks/useKeyDown';
import useNavigator from '../../hooks/useNavigator';
import useToast from '../../hooks/useToast';
import { TopicCardProps } from '../../types/Topic';
import AddSeeTogether from '../AddSeeTogether';
import Box from '../common/Box';
import Flex from '../common/Flex';
import Image from '../common/Image';
import Space from '../common/Space';
import MediaText from '../common/Text/MediaText';

interface OnClickDesignatedProps {
  topicId: number;
  topicName: string;
}

interface TopicCardExtendedProps extends TopicCardProps {
  cardType: 'default' | 'modal';
  onClickDesignated?: ({ topicId, topicName }: OnClickDesignatedProps) => void;
  getTopicsFromServer?: (
    options?: RefetchOptions | undefined,
  ) => Promise<QueryObserverResult<TopicCardProps[], Error>>;
}

function TopicCard({
  cardType,
  id,
  image,
  creator,
  name,
  updatedAt,
  pinCount,
  bookmarkCount,
  isInAtlas,
  onClickDesignated,
  getTopicsFromServer,
}: TopicCardExtendedProps) {
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
      return true;
    }
    showToast('info', '해당 지도를 모아보기에서 제외했습니다.');
    return true;
  };
  return (
    <Wrapper
      data-cy="topic-card"
      onClick={cardType === 'default' ? goToSelectedTopic : addPinToThisTopic}
      ref={elementRef}
      onKeyDown={onElementKeyDown}
    >
      <Flex
        as="article"
        $flexDirection="column"
        position="relative"
        tabIndex={0}
        role="button"
      >
        <Image
          width="100%"
          height="100%"
          src={image}
          alt="사진 이미지"
          $objectFit="cover"
          $errorDefaultSrc={DEFAULT_TOPIC_IMAGE}
          radius="small"
          ratio="1.6 / 1"
          isAriaHidden
        />

        <Box width="100%" $maxWidth="212px" padding={1}>
          <Box height="52px">
            <MediaText
              color="black"
              $fontSize="default"
              $fontWeight="bold"
              aria-label={`지도 이름은 ${name} 입니다.`}
            >
              {name}
            </MediaText>
          </Box>

          <MediaText
            color="black"
            $fontSize="small"
            $fontWeight="normal"
            aria-label={`작성자는 ${creator} 이며`}
          >
            {creator}
          </MediaText>

          <Space size={0} />

          <MediaText
            color="gray"
            $fontSize="small"
            $fontWeight="normal"
            aria-label={`${updatedAt
              .split('T')[0]
              .replaceAll('-', '.')}에 마지막으로 업데이트 되었습니다.`}
          >
            {updatedAt.split('T')[0].replaceAll('-', '.')} 업데이트
          </MediaText>

          <Space size={0} />

          <Flex>
            <Flex $alignItems="center" width="64px">
              <SmallTopicPin />
              <Space size={0} />
              <MediaText
                color="black"
                $fontSize="extraSmall"
                $fontWeight="normal"
                aria-label={`핀 갯수는 ${pinCount}개 이며`}
              >
                {pinCount > 999 ? '+999' : pinCount}개
              </MediaText>
            </Flex>
            <Flex $alignItems="center" width="64px">
              <SmallTopicStar />
              <Space size={0} />
              <MediaText
                color="black"
                $fontSize="extraSmall"
                $fontWeight="normal"
                aria-label={`즐겨찾기는 ${bookmarkCount}명 입니다.`}
              >
                {bookmarkCount > 999 ? '+999' : bookmarkCount}명
              </MediaText>
            </Flex>
          </Flex>

          {cardType === 'default' && getTopicsFromServer && (
            <ButtonWrapper>
              <AddSeeTogether
                parentType="topicCard"
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
            </ButtonWrapper>
          )}
        </Box>
      </Flex>
    </Wrapper>
  );
}

const Wrapper = styled.li`
  cursor: pointer;
  border-radius: ${({ theme }) => theme.radius.small};
`;

const ButtonWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  position: absolute;
  width: 32px;

  top: 4%;
  right: 4%;
`;

export default TopicCard;
