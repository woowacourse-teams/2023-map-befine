import { styled } from 'styled-components';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import { FULLSCREEN } from '../constants';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import Box from '../components/common/Box';
import { Suspense, lazy } from 'react';
import TopicCardContainerSkeleton from '../components/TopicCardContainer/TopicCardContainerSkeleton';

const SeeAllCardList = lazy(() => import('../components/SeeAllCardList'));

const url = '/topics/newest';

const SeeAllLatestTopics = () => {
  const { width: _ } = useSetLayoutWidth(FULLSCREEN);
  const { navbarHighlights: __ } = useSetNavbarHighlight('home');

  return (
    <Wrapper>
      <Space size={5} />
      <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
        새로울 지도?
      </Text>

      <Space size={5} />

      <Suspense fallback={<TopicCardContainerSkeleton />}>
        <SeeAllCardList url={url} />
      </Suspense>
    </Wrapper>
  );
};

const Wrapper = styled(Box)`
  width: 1036px;
  margin: 0 auto;
`;

export default SeeAllLatestTopics;
