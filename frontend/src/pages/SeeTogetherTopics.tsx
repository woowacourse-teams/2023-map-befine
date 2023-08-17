import { useContext, useEffect, useState } from 'react';
import { SIDEBAR } from '../constants';
import useNavigator from '../hooks/useNavigator';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import { SeeTogetherContext } from '../context/SeeTogetherContext';
import Text from '../components/common/Text';
import SeeTogetherNotFilledSVG from '../assets/seeTogetherBtn_notFilled.svg';
import { styled } from 'styled-components';
import Flex from '../components/common/Flex';
import Space from '../components/common/Space';
import TopicCard from '../components/TopicCard';
import Button from '../components/common/Button';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';
import { deleteApi } from '../apis/deleteApi';
import useToast from '../hooks/useToast';
import { getApi } from '../apis/getApi';
import { TopicType } from '../types/Topic';

const SeeTogetherTopics = () => {
  const { routePage } = useNavigator();
  const { width } = useSetLayoutWidth(SIDEBAR);
  const { navbarHighlights } = useSetNavbarHighlight('seeTogether');
  const { seeTogetherTopics, setSeeTogetherTopics } =
    useContext(SeeTogetherContext);
  const { showToast } = useToast();

  const goToHome = () => {
    routePage('/');
  };

  const setTopicsFromServer = async () => {
    try {
      const topics = await getApi<TopicType[]>('default', '/members/my/atlas');

      setSeeTogetherTopics(topics);
    } catch {
      showToast('error', '로그인 후 이용해주세요.');
    }
  };

  const goToSelectedTopic = () => {
    if (!seeTogetherTopics) return;

    const seeTogetherTopicIds = seeTogetherTopics
      .map((topic) => topic.id)
      .join(',');

    routePage(`/topics/${seeTogetherTopicIds}`, seeTogetherTopicIds);
  };

  const onClickDeleteSeeTogetherTopics = () => {
    if (!seeTogetherTopics) return;

    const deleteTopics = seeTogetherTopics;

    try {
      deleteTopics.forEach(async (topic) => {
        await deleteApi(`/atlas/topics?id=${topic.id}`);
      });

      showToast('info', '모아보기를 비웠습니다.');

      setSeeTogetherTopics([]);
    } catch (e) {
      showToast('info', '모아보기를 비우는데 실패했습니다.');
    }
  };

  if (!seeTogetherTopics) return <></>;

  if (seeTogetherTopics.length === 0) {
    return (
      <WrapperWhenEmpty width={width}>
        <Flex $alignItems="center">
          <SeeTogetherNotFilledSVG />
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
      <Space size={5} />
      {seeTogetherTopics.map((topic, idx) => (
        <ul key={topic.id}>
          <TopicCard
            id={topic.id}
            image={topic.image}
            name={topic.name}
            creator={topic.creator}
            pinCount={topic.pinCount}
            bookmarkCount={topic.bookmarkCount}
            updatedAt={topic.updatedAt}
            isInAtlas={topic.isInAtlas}
            isBookmarked={topic.isBookmarked}
            setTopicsFromServer={setTopicsFromServer}
          />
          {idx !== seeTogetherTopics.length - 1 ? <Space size={4} /> : <></>}
        </ul>
      ))}

      <Space size={6} />

      <ButtonsWrapper>
        <Button variant="secondary" onClick={onClickDeleteSeeTogetherTopics}>
          비우기
        </Button>
        <Space size={3} />
        <Button variant="primary" onClick={goToSelectedTopic}>
          한 번에 보기
        </Button>
      </ButtonsWrapper>
      <Space size={5} />
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

const ButtonsWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

export default SeeTogetherTopics;
