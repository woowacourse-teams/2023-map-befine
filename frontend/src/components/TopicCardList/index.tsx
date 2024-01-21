import { ReactNode } from 'react';
import useGetTopics from '../../apiHooks/new/useGetTopics';
import Button from '../common/Button';
import Flex from '../common/Flex';
import Grid from '../common/Grid';
import Space from '../common/Space';
import Text from '../common/Text';
import TopicCard from '../TopicCard';
import useProfileList from '../../hooks/queries/useProfileList';

interface TopicCardListProps {
  url: string;
  commentWhenEmpty: string;
  routePageName: string;
  routePage: () => void;
  svgElementWhenEmpty?: ReactNode;
}

function TopicCardList({
  url,
  commentWhenEmpty,
  routePageName,
  routePage,
  svgElementWhenEmpty,
}: TopicCardListProps) {
  const { topics, refetch } = useGetTopics(url);

  if (topics.length === 0) {
    return (
      <Flex height="240px" $flexDirection="column" $alignItems="center">
        <Flex $alignItems="center">
          {svgElementWhenEmpty}
          <Space size={1} />
          <Text color="black" $fontSize="default" $fontWeight="normal">
            {commentWhenEmpty}
          </Text>
          <Space size={4} />
        </Flex>
        <Space size={5} />
        <Button variant="primary" onClick={routePage}>
          {routePageName}
        </Button>
      </Flex>
    );
  }

  return (
    <Flex $flexWrap="wrap" $gap="20px">
      <Grid
        rows="auto"
        columns={5}
        gap={20}
        width="100%"
        $mediaQueries={[
          [1180, 4],
          [900, 3],
          [660, 2],
          [320, 1],
        ]}
      >
        {topics.map((topic) => (
          <ul key={topic.id}>
            <TopicCard
              cardType="default"
              id={topic.id}
              image={topic.image}
              name={topic.name}
              creator={topic.creator}
              updatedAt={topic.updatedAt}
              pinCount={topic.pinCount}
              bookmarkCount={topic.bookmarkCount}
              isInAtlas={topic.isInAtlas}
              isBookmarked={topic.isBookmarked}
              getTopicsFromServer={refetch}
            />
          </ul>
        ))}
      </Grid>
    </Flex>
  );
}

export default TopicCardList;
