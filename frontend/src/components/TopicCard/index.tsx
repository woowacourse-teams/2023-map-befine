import { styled } from 'styled-components';
import Text from '../common/Text';
import useNavigator from '../../hooks/useNavigator';
import Box from '../common/Box';
import Image from '../common/Image';
import { SyntheticEvent } from 'react';
import Space from '../common/Space';

export interface TopicCardProps {
  topicId: number;
  topicImage: string;
  topicTitle: string;
  topicUpdatedAt: string;
  topicPinCount: number;
}

const TopicCard = ({
  topicId,
  topicImage,
  topicTitle,
  topicUpdatedAt,
  topicPinCount,
}: TopicCardProps) => {
  const { routePage } = useNavigator();

  const goToSelectedTopic = () => {
    routePage(`topics/${topicId}`, [topicId]);
  };

  return (
    <Wrapper onClick={goToSelectedTopic}>
      <Box>
        <TopicImage
          width="180px"
          height="180px"
          src={topicImage}
          alt="토픽 이미지"
          $objectFit="cover"
          onError={(e: SyntheticEvent<HTMLImageElement, Event>) => {
            e.currentTarget.src =
              'https://velog.velcdn.com/images/semnil5202/post/37dae18f-9860-4483-bad5-1158a210e5a8/image.svg';
          }}
        />
        <Space size={0} />
        <Text color="black" $fontSize="medium" $fontWeight="bold">
          &nbsp; {topicTitle}
        </Text>
        <Space size={1} />
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          &nbsp; {topicPinCount}개의 핀
        </Text>
        <Text color="gray" $fontSize="small" $fontWeight="normal">
          &nbsp; {topicUpdatedAt.split('T')[0].replaceAll('-', '.')}
        </Text>
      </Box>
    </Wrapper>
  );
};

const Wrapper = styled.li`
  width: 180px;
  height: 280px;
  box-shadow: 2px 4px 4px 2px rgba(69, 69, 69, 0.25);
  cursor: pointer;
  border-radius: ${({ theme }) => theme.radius.small};
`;

const TopicImage = styled(Image)`
  border-top-left-radius: ${({ theme }) => theme.radius.small};
  border-top-right-radius: ${({ theme }) => theme.radius.small};
`;

export default TopicCard;
