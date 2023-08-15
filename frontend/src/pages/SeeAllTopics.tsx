import { useLocation } from 'react-router-dom';
import Text from '../components/common/Text';
import { lazy, Suspense } from 'react';
import SeeAllCardListSkeleton from '../components/SeeAllCardList/SeeAllCardListSkeleton';

const SeeAllCardList = lazy(() => import('../components/SeeAllCardList'));

const SeeAllTopics = () => {
  const { state } = useLocation();
  const url = state.split('|')[0];
  const title = state.split('|')[1];

  return (
    <>
      <Text color="black" $fontSize="large" $fontWeight="bold">
        {title}
      </Text>
      <Suspense fallback={<SeeAllCardListSkeleton />}>
        <SeeAllCardList url={url} />
      </Suspense>
    </>
  );
};

export default SeeAllTopics;
