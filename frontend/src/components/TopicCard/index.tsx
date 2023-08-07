import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Text from '../common/Text';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';
import Image from '../common/Image';
import {
  KeyboardEvent,
  SyntheticEvent,
  useEffect,
  useRef,
  useState,
} from 'react';

export interface TopicCardProps {
  topicId: number;
  topicImage: string;
  topicTitle: string;
  topicUpdatedAt: string;
  topicPinCount: number;
  tagTopics: string[];
  setTagTopics: React.Dispatch<React.SetStateAction<string[]>>;
  taggedTopicIds: number[];
  setTaggedTopicIds: React.Dispatch<React.SetStateAction<number[]>>;
}

const TopicCard = ({
  topicId,
  topicImage,
  topicTitle,
  topicUpdatedAt,
  topicPinCount,
  tagTopics,
  setTagTopics,
  taggedTopicIds,
  setTaggedTopicIds,
}: TopicCardProps) => {
  const { routePage } = useNavigator();

  const inputRef = useRef<HTMLInputElement | null>(null);

  const divRef = useRef<HTMLDivElement | null>(null);

  const goToSelectedTopic = () => {
    routePage(`topics/${topicId}`, [topicId]);
  };

  const [announceText, setAnnounceText] = useState<string>('토픽 카드 선택');

  const onAddTagOfTopic = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.checked) {
      setTagTopics([...tagTopics, topicTitle]);
      setTaggedTopicIds((prev) => [...prev, topicId]);

      if (taggedTopicIds.length === 0) {
        setAnnounceText(
          `토픽 ${topicTitle}이 태그에 추가됨. 같이보기 및 병합 기능 활성화`,
        );
        return;
      }
      setAnnounceText(`토픽 ${topicTitle}이 태그에 추가됨`);
    } else {
      setTagTopics(tagTopics.filter((value) => value !== topicTitle));
      setTaggedTopicIds(taggedTopicIds.filter((value) => value !== topicId));

      if (taggedTopicIds.length === 1) {
        setAnnounceText(
          `토픽 ${topicTitle}이 태그에서 삭제됨. 같이보기 및 병합 버튼 비활성화`,
        );
        return;
      }
      setAnnounceText(`토픽 ${topicTitle}이 태그에서 삭제됨`);
    }
  };

  const onInputKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.keyCode === 13) {
      e.preventDefault();
      inputRef.current?.click();
    }
  };

  const onDivKeyDown = (e: KeyboardEvent<HTMLDivElement>) => {
    if (e.keyCode === 13) {
      e.preventDefault();
      divRef.current?.click();
    }
  };

  useEffect(() => {
    if (announceText) {
      const liveRegion = document.getElementById('live-region');
      if (liveRegion) {
        liveRegion.innerText = announceText;
      }
    }
  }, [announceText]);

  return (
    <Wrapper>
      <Flex
        width="360px"
        height="140px"
        position="relative"
        $flexDirection="column"
        $alignItems="center"
        $justifyContent="center"
        $borderRadius="small"
        $backgroundColor="whiteGray"
      >
        <Box position="absolute">
          <Image
            width="360px"
            height="140px"
            src={topicImage}
            alt="토픽 이미지"
            $objectFit="cover"
            onError={(e: SyntheticEvent<HTMLImageElement, Event>) => {
              e.currentTarget.src =
                'https://velog.velcdn.com/images/semnil5202/post/37dae18f-9860-4483-bad5-1158a210e5a8/image.svg';
            }}
          />
        </Box>
        <MultiSelectButton
          type="checkbox"
          onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
            onAddTagOfTopic(e)
          }
          onKeyDown={onInputKeyDown}
          checked={taggedTopicIds.includes(topicId)}
          aria-label={`${topicTitle} 토픽 카드 선택`}
          ref={inputRef}
        />
        <Box
          position="absolute"
          width="100%"
          height="68px"
          $backgroundColor="black"
          opacity="0.6"
        ></Box>
        <Flex
          width="100%"
          height="68px"
          $flexDirection="column"
          $alignItems="center"
          $justifyContent="center"
          cursor="pointer"
          onClick={goToSelectedTopic}
          onKeyDown={onDivKeyDown}
          $backdropFilter="blur(12px)"
          tabIndex={0}
          role="button"
          ref={divRef}
        >
          <Text color="white" $fontSize="medium" $fontWeight="normal">
            {topicTitle}
          </Text>
          <Text color="lightGray" $fontSize="small" $fontWeight="normal">
            {`업데이트 : ${
              topicUpdatedAt.split('T')[0]
            } | 핀 개수 : ${topicPinCount}`}
          </Text>
        </Flex>
      </Flex>
      <div
        id="live-region"
        aria-live="assertive"
        style={{ position: 'absolute', left: '-9999px' }}
      ></div>
    </Wrapper>
  );
};

const Wrapper = styled.li`
  box-shadow: 2px 4px 4px 0px rgba(69, 69, 69, 0.25);
`;

const MultiSelectButton = styled.input`
  width: 24px;
  height: 24px;
  position: absolute;
  top: ${({ theme }) => theme.spacing[0]};
  right: ${({ theme }) => theme.spacing[0]};
  background-color: ${({ theme }) => theme.color.white};
  border: 1px solid ${({ theme }) => theme.color.black};
  border-radius: ${({ theme }) => theme.radius.small};
  cursor: pointer;

  &:focus {
    background-color: ${({ theme }) => theme.color.primary};
  }
`;

export default TopicCard;
