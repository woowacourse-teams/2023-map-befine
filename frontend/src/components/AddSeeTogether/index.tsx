import { styled } from 'styled-components';
import { postApi } from '../../apis/postApi';
import useToast from '../../hooks/useToast';
import { useContext } from 'react';
import { getApi } from '../../apis/getApi';
import { TopicType } from '../../types/Topic';
import { SeeTogetherContext } from '../../context/SeeTogetherContext';
import { deleteApi } from '../../apis/deleteApi';

interface AddSeeTogetherProps {
  isInAtlas: boolean;
  id: number;
  children: React.ReactNode;
  setTopicsFromServer: () => void;
}

const AddSeeTogether = ({
  isInAtlas,
  id,
  children,
  setTopicsFromServer,
}: AddSeeTogetherProps) => {
  const { showToast } = useToast();
  const { seeTogetherTopics, setSeeTogetherTopics } =
    useContext(SeeTogetherContext);

  const addSeeTogetherList = async (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();

    try {
      if (seeTogetherTopics && seeTogetherTopics.length === 7) {
        showToast('warning', '모아보기는 7개까지만 가능합니다.');
        return;
      }

      await postApi(`/atlas/topics?id=${id}`, {}, 'x-www-form-urlencoded');

      const topics = await getApi<TopicType[]>('/members/my/atlas');

      setSeeTogetherTopics(topics);

      // TODO: 모아보기 페이지에서는 GET /members/my/atlas 두 번 됨
      setTopicsFromServer();

      showToast('info', '모아보기에 추가했습니다.');
    } catch {
      showToast('error', '로그인 후 사용해주세요.');
    }
  };

  const deleteSeeTogether = async (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();

    try {
      await deleteApi(`/atlas/topics?id=${id}`, 'x-www-form-urlencoded');

      const topics = await getApi<TopicType[]>('/members/my/atlas');

      setSeeTogetherTopics(topics);

      // TODO: 모아보기 페이지에서는 GET /members/my/atlas 두 번 됨
      setTopicsFromServer();

      showToast('info', '해당 지도를 모아보기에서 제외했습니다.');
    } catch {
      showToast('error', '로그인 후 사용해주세요.');
    }
  };

  return (
    <Wrapper onClick={isInAtlas ? deleteSeeTogether : addSeeTogetherList}>
      {children}
    </Wrapper>
  );
};

const Wrapper = styled.div`
  cursor: pointer;

  &:hover {
    filter: brightness(0.9);
  }
`;

export default AddSeeTogether;
