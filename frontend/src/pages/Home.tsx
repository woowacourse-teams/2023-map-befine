import Space from '../components/common/Space';
import Box from '../components/common/Box';
import useNavigator from '../hooks/useNavigator';
import TopicListContainer from '../components/TopicListContainer';
import { styled } from 'styled-components';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { FULLSCREEN } from '../constants';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import useToast from '../hooks/useToast';
import { TopicCardProps } from '../types/Topic';
import { getApi } from '../apis/getApi';
import { useEffect, useState } from 'react';
import Text from '../components/common/Text';
import useGet from '../apiHooks/useGet';

const Home = () => {
  const [popularTopics, setPopularTopics] = useState<TopicCardProps[] | null>(
    null,
  );
  const [nearTopics, setNearTopics] = useState<TopicCardProps[] | null>(null);
  const [newestTopics, setNewestTopics] = useState<TopicCardProps[] | null>(
    null,
  );
  const { routePage } = useNavigator();
  const { width: _ } = useSetLayoutWidth(FULLSCREEN);
  const { navbarHighlights: __ } = useSetNavbarHighlight('home');
  const { showToast } = useToast();
  const { fetchGet } = useGet();

  const goToPopularTopics = () => {
    routePage('see-all/popularity');
  };

  const goToNearByMeTopics = () => {
    routePage('see-all/near');
  };

  const goToLatestTopics = () => {
    routePage('see-all/latest');
  };

  const getNearTopicsFromServer = async () => {
    try {
      const topics = await getApi<TopicCardProps[]>(`/topics`);
      setNearTopics(topics);
    } catch {
      showToast(
        'error',
        '로그인 정보가 만료되었습니다. 로그아웃 후 다시 로그인 해주세요.',
      );
    }
  };

  const getNewestTopicsFromServer = async () => {
    try {
      const topics = await getApi<TopicCardProps[]>('/topics/newest');
      setNewestTopics(topics);
    } catch {
      showToast(
        'error',
        '로그인 정보가 만료되었습니다. 로그아웃 후 다시 로그인 해주세요.',
      );
    }
  };

  const getPopularTopicsFromServer = async () => {
    try {
      const topics = await getApi<TopicCardProps[]>('/topics/bests');
      setPopularTopics(topics);
    } catch {
      showToast(
        'error',
        '로그인 정보가 만료되었습니다. 로그아웃 후 다시 로그인 해주세요.',
      );
    }
  };

  const topicsFetchingFromServer = async () => {
    await getPopularTopicsFromServer();
    await getNearTopicsFromServer();
    await getNewestTopicsFromServer();
  };

  useEffect(() => {
    topicsFetchingFromServer();
  }, []);

  if (!(popularTopics && nearTopics && newestTopics)) return <></>;

  if (
    popularTopics.length === 0 &&
    nearTopics.length === 0 &&
    newestTopics.length === 0
  ) {
    return (
      <EmptyWrapper>
        <Text color="primary" $fontSize="extraLarge" $fontWeight="bold">
          추가하기 버튼을 눌러 토픽을 추가해보세요!
        </Text>
        <Space size={1} />
        <Text color="black" $fontSize="default" $fontWeight="normal">
          토픽이 없습니다.
        </Text>
      </EmptyWrapper>
    );
  }

  return (
    <>
      <Wrapper position="relative">
        <Space size={5} />
        <TopicListContainer
          containerTitle="인기 급상승할 지도?"
          containerDescription="즐겨찾기가 많이 된 지도를 확인해보세요."
          routeWhenSeeAll={goToPopularTopics}
          topics={popularTopics}
          setTopicsFromServer={topicsFetchingFromServer}
        />
        <Space size={9} />
        <TopicListContainer
          containerTitle="새로울 지도?"
          containerDescription="방금 새로운 핀이 추가된 지도를 확인해보세요."
          routeWhenSeeAll={goToLatestTopics}
          topics={newestTopics}
          setTopicsFromServer={topicsFetchingFromServer}
        />
        <Space size={9} />
        <TopicListContainer
          containerTitle="모두일 지도?"
          containerDescription="괜찮을지도의 모든 지도를 확인해보세요."
          routeWhenSeeAll={goToNearByMeTopics}
          topics={nearTopics}
          setTopicsFromServer={topicsFetchingFromServer}
        />
        <Space size={5} />
      </Wrapper>
    </>
  );
};

const EmptyWrapper = styled.section`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 1036px;
  height: 100vh;
  margin: 0 auto;
`;

const Wrapper = styled(Box)`
  width: 1036px;
  margin: 0 auto;
`;

const ModalWrapper = styled.div`
  width: 300px;
  height: 300px;
  background-color: white;
`;

export default Home;
