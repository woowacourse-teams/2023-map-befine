import Text from '../components/common/Text';
import Space from '../components/common/Space';
import { styled } from 'styled-components';
import Box from '../components/common/Box';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { FULLSCREEN } from '../constants';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import { Suspense, lazy } from 'react';
import TopicCardContainerSkeleton from '../components/TopicCardContainer/TopicCardContainerSkeleton';

const SeeAllCardList = lazy(() => import('../components/SeeAllCardList'));

const url = '/topics/bests';

const SeeAllTopics = () => {
  const { width: _ } = useSetLayoutWidth(FULLSCREEN);
  const { navbarHighlights: __ } = useSetNavbarHighlight('home');

  return (
    <Wrapper>
      <Space size={5} />
      <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
        인기 급상승할 지도?
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

export default SeeAllTopics;
