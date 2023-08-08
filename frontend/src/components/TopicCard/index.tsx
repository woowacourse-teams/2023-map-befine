import { styled } from 'styled-components';
import Text from '../common/Text';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';
import Image from '../common/Image';
import { SyntheticEvent } from 'react';
import Space from '../common/Space';
import Flex from '../common/Flex';

export interface TopicCardProps {
  topicSize: 'vertical' | 'horizontal';
  topicId: number;
  topicImage: string;
  topicTitle: string;
  topicUpdatedAt: string;
  topicPinCount: number;
}

const TopicCard = ({
  topicSize,
  topicId,
  topicImage,
  topicTitle,
  topicUpdatedAt,
  topicPinCount,
}: TopicCardProps) => {
  const { routePage } = useNavigator();

  const goToSelectedTopic = () => {
    routePage(`/topics/${topicId}`, [topicId]);
  };

  if (topicSize === 'horizontal') {
    return (
      <HorizontalWrapper onClick={goToSelectedTopic}>
        <Flex>
          <HorizontalTopicImage
            height="148px"
            width="148px"
            src={topicImage}
            alt="토픽 이미지"
            $objectFit="cover"
            onError={(e: SyntheticEvent<HTMLImageElement, Event>) => {
              e.currentTarget.src =
                'https://velog.velcdn.com/images/semnil5202/post/37dae18f-9860-4483-bad5-1158a210e5a8/image.svg';
            }}
          />
          <Box width="212px" padding={1}>
            <Box height="60px">
              <Text color="black" $fontSize="medium" $fontWeight="bold">
                {topicTitle}
              </Text>
            </Box>
            <Text color="black" $fontSize="small" $fontWeight="normal">
              토픽 생성자
            </Text>
            <Space size={0} />
            <Text color="gray" $fontSize="small" $fontWeight="normal">
              핀 {topicPinCount}개
            </Text>
            <Text color="gray" $fontSize="small" $fontWeight="normal">
              즐겨찾기 10명
            </Text>
          </Box>
        </Flex>
      </HorizontalWrapper>
    );
  }

  return (
    <VerticalWrapper onClick={goToSelectedTopic}>
      <Box>
        <VerticalTopicImage
          width="172px"
          height="172px"
          src={topicImage}
          alt="토픽 이미지"
          $objectFit="cover"
          onError={(e: SyntheticEvent<HTMLImageElement, Event>) => {
            e.currentTarget.src =
              'https://velog.velcdn.com/images/semnil5202/post/37dae18f-9860-4483-bad5-1158a210e5a8/image.svg';
          }}
        />
        <Box padding={1}>
          <Space size={0} />
          <Box height="60px">
            <Text color="black" $fontSize="medium" $fontWeight="bold">
              {topicTitle}
            </Text>
          </Box>
          <Text color="gray" $fontSize="small" $fontWeight="normal">
            {`핀 ${topicPinCount}개 | 
            ${topicUpdatedAt.split('T')[0].replaceAll('-', '.')}`}
          </Text>
        </Box>
      </Box>
    </VerticalWrapper>
  );
};

const VerticalWrapper = styled.li`
  width: 172px;
  height: 276px;
  box-shadow: 2px 4px 4px 2px rgba(69, 69, 69, 0.25);
  cursor: pointer;
  border-radius: ${({ theme }) => theme.radius.small};
`;

const HorizontalWrapper = styled.li`
  width: 352px;
  height: 148px;
  box-shadow: 2px 4px 4px 2px rgba(69, 69, 69, 0.25);
  cursor: pointer;
  border-radius: ${({ theme }) => theme.radius.small};
`;

const VerticalTopicImage = styled(Image)`
  border-top-left-radius: ${({ theme }) => theme.radius.small};
  border-top-right-radius: ${({ theme }) => theme.radius.small};
`;

const HorizontalTopicImage = styled(Image)`
  border-radius: ${({ theme }) => theme.radius.small};
`;

export default TopicCard;
