import { styled } from 'styled-components';
import { postApi } from '../../apis/postApi';
import useToast from '../../hooks/useToast';
import { useContext, useState } from 'react';
import { getApi } from '../../apis/getApi';
import { TopicType } from '../../types/Topic';
import { SeeTogetherContext } from '../../context/SeeTogetherContext';

interface AddSeeTogetherProps {
  id: number;
  children: React.ReactNode;
}

const AddSeeTogether = ({ id, children }: AddSeeTogetherProps) => {
  const { showToast } = useToast();
  const { seeTogetherTopics, setSeeTogetherTopics } =
    useContext(SeeTogetherContext);

  const addSeeTogetherList = async (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation();

    try {
      if (seeTogetherTopics.length === 7) {
        showToast('warning', '모아보기는 7개까지만 가능합니다.');
        return;
      }

      // await postApi('',{});
      // const topics = await getApi<TopicType[]>('default', '/members/atlas');
      // setSeeTogetherTopics(topics);

      showToast('info', '모아보기에 추가했습니다.');
    } catch {
      showToast(
        'error',
        '모아보기 추가에 실패했습니다. 네트워크를 확인해주세요',
      );
    }
  };

  return <Wrapper onClick={addSeeTogetherList}>{children}</Wrapper>;
};

const Wrapper = styled.div`
  cursor: pointer;

  &:hover {
    filter: brightness(0.9);
  }
`;

export default AddSeeTogether;
