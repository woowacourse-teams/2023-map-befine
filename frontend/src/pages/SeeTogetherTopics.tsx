import { useContext, useEffect } from 'react';
import { SIDEBAR } from '../constants';
import useNavigator from '../hooks/useNavigator';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { SeeTogetherContext } from '../context/SeeTogetherContext';
import Text from '../components/common/Text';
import SeeTogetherSVG from '../assets/seeTogetherBtn_filled.svg';

import { styled } from 'styled-components';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import TopicCard from '../components/TopicCard';
import Button from '../components/common/Button';
import { TopicType } from '../types/Topic';
import { getApi } from '../apis/getApi';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

const SeeTogetherTopics = () => {
  const { routePage } = useNavigator();
  const { width } = useSetLayoutWidth(SIDEBAR);
  const { navbarHighlights } = useSetNavbarHighlight('seeTogether');
  const { seeTogetherTopics, setSeeTogetherTopics } =
    useContext(SeeTogetherContext);

  const goToHome = () => {
    routePage('/');
  };

  const goToSeveralSelectedTopics = () => {
    routePage(
      `/topics/${seeTogetherTopics.map((topic) => topic.id).join(',')}`,
      seeTogetherTopics.map((topic) => topic.id).join(','),
    );
  };

  const getSeeTogetherTopics = async () => {
    const topics = await getApi<TopicType[]>('default', '/members/atlas');

    setSeeTogetherTopics(topics);
  };

  useEffect(() => {
    // getSeeTogetherTopics();
  }, []);

  if (seeTogetherTopics.length === 0) {
    return (
      <WrapperWhenEmpty width={width}>
        <Flex $alignItems="center">
          <SeeTogetherSVG />
          <Space size={1} />
          <Text color="black" $fontSize="default" $fontWeight="normal">
            버튼을 눌러 지도를 추가해보세요.
          </Text>
          <Space size={4} />
        </Flex>
        <Space size={5} />
        <Button variant="primary" onClick={goToHome}>
          메인페이지로 가기
        </Button>
      </WrapperWhenEmpty>
    );
  }

  return (
    <Wrapper width={width}>
      {seeTogetherTopics.map((topic, idx) => (
        <ul key={topic.id}>
          <TopicCard
            topicId={topic.id}
            topicImage={''}
            topicTitle={topic.name}
            topicPinCount={topic.pinCount}
            topicUpdatedAt={topic.updatedAt}
          />
          {idx !== seeTogetherTopics.length - 1 ? <Space size={4} /> : <></>}
        </ul>
      ))}

      <Space size={6} />

      <Flex $justifyContent="center">
        <Button variant="secondary">비우기</Button>
        <Space size={3} />
        <Button variant="primary" onClick={goToSeveralSelectedTopics}>
          한 번에 보기
        </Button>
      </Flex>
    </Wrapper>
  );
};

const Wrapper = styled.section<{ width: '372px' | '100vw' }>`
  width: ${({ width }) => `calc(${width} - 40px)`};
  height: 100%;
  display: flex;
  flex-direction: column;
`;

const WrapperWhenEmpty = styled.section<{ width: '372px' | '100vw' }>`
  width: ${({ width }) => `calc(${width} - 40px)`};
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

export default SeeTogetherTopics;
