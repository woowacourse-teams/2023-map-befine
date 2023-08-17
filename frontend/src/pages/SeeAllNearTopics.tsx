import { styled } from 'styled-components';
import { FULLSCREEN } from '../constants';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import Box from '../components/common/Box';
import Space from '../components/common/Space';
import Text from '../components/common/Text';
import TopicCardListSkeleton from '../components/TopicCardList/TopicCardListSkeleton';
import { Suspense, lazy } from 'react';

const SeeAllCardList = lazy(() => import('../components/SeeAllCardList'));

const url = '/topics';

const SeeAllNearTopics = () => {
  const { width: _ } = useSetLayoutWidth(FULLSCREEN);
  const { navbarHighlights: __ } = useSetNavbarHighlight('home');

  return (
    <Wrapper>
      <Space size={5} />
      <Text color="black" $fontSize="extraLarge" $fontWeight="bold">
        내 주변일 지도?
      </Text>

      <Space size={5} />

      <Suspense fallback={<TopicCardListSkeleton />}>
        <SeeAllCardList url={url} />
      </Suspense>
    </Wrapper>
  );
};

const Wrapper = styled(Box)`
  width: 1036px;
  margin: 0 auto;
`;

export default SeeAllNearTopics;
