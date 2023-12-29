import { useContext } from 'react';
import { styled } from 'styled-components';

import { deleteApi } from '../../apis/deleteApi';
import { getApi } from '../../apis/getApi';
import { postApi } from '../../apis/postApi';
import { ARIA_FOCUS } from '../../constants';
import { SeeTogetherContext } from '../../context/SeeTogetherContext';
import useNavigator from '../../hooks/useNavigator';
import useToast from '../../hooks/useToast';
import { TopicCardProps } from '../../types/Topic';

interface AddSeeTogetherProps {
  parentType: 'topicCard' | 'topicInfo';
  isInAtlas: boolean;
  id: number;
  children: React.ReactNode;
  getTopicsFromServer: () => void;
  onClickAtlas: () => boolean;
}

function AddSeeTogether({
  parentType,
  isInAtlas,
  id,
  children,
  getTopicsFromServer,
  onClickAtlas,
}: AddSeeTogetherProps) {
  const { showToast } = useToast();
  const { seeTogetherTopics, setSeeTogetherTopics } =
    useContext(SeeTogetherContext);
  const { routePage } = useNavigator();

  const accessToken = localStorage.getItem('userToken');

  const addSeeTogetherList = async (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();

    try {
      if (seeTogetherTopics && seeTogetherTopics.length === 7) {
        showToast('error', '모아보기는 7개까지만 가능합니다.');
        return;
      }

      await postApi(`/atlas/topics?id=${id}`, {}, 'x-www-form-urlencoded');

      const topics = await getApi<TopicCardProps[]>('/members/my/atlas');

      setSeeTogetherTopics(topics.map((topic) => topic.id));

      // TODO: 모아보기 페이지에서는 GET /members/my/atlas 두 번 됨
      getTopicsFromServer();
      onClickAtlas();

      showToast('info', '모아보기에 추가했습니다.');
    } catch {
      showToast('error', '로그인 후 사용해주세요.');
    }
  };

  const deleteSeeTogether = async (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();

    try {
      await deleteApi(`/atlas/topics?id=${id}`, 'x-www-form-urlencoded');

      const topics = await getApi<TopicCardProps[]>('/members/my/atlas');

      setSeeTogetherTopics(topics.map((topic) => topic.id));

      // TODO: 모아보기 페이지에서는 GET /members/my/atlas 두 번 됨
      getTopicsFromServer();
      onClickAtlas();

      showToast('info', '해당 지도를 모아보기에서 제외했습니다.');

      if (parentType === 'topicInfo') routePageAfterSuccessToDelete(topics);
    } catch {
      showToast('error', '로그인 후 사용해주세요.');
    }
  };

  const routePageAfterSuccessToDelete = (topics: TopicCardProps[]) => {
    if (topics.length === 0) {
      routePage(`/`);
      return;
    }

    routePage(`/see-together/${topics.map((topic) => topic.id).join(',')}`);
  };

  const onChangeIsInAtlas = (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();

    const isChangeAtlas = onClickAtlas();

    if (isChangeAtlas) {
      if (seeTogetherTopics?.includes(id))
        setSeeTogetherTopics(
          seeTogetherTopics.filter((topicId) => topicId !== id),
        );
      else setSeeTogetherTopics((prev) => [...prev, id]);
    }
  };

  if (accessToken === null) {
    return <Wrapper onClick={onChangeIsInAtlas}>{children}</Wrapper>;
  }

  return (
    <Wrapper
      tabIndex={ARIA_FOCUS}
      role="button"
      aria-label="모아보기 추가 버튼"
      onClick={isInAtlas ? deleteSeeTogether : addSeeTogetherList}
    >
      {children}
    </Wrapper>
  );
}

const Wrapper = styled.div`
  cursor: pointer;

  &:hover {
    filter: brightness(0.9);
  }
`;

export default AddSeeTogether;
