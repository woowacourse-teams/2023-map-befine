import { useContext, useEffect } from 'react';
import { keyframes, styled } from 'styled-components';

import { getApi } from '../../apis/getApi';
import { SeeTogetherContext } from '../../context/SeeTogetherContext';
import useToast from '../../hooks/useToast';
import { TopicCardProps } from '../../types/Topic';

function SeeTogetherCounter() {
  const { seeTogetherTopics, setSeeTogetherTopics } =
    useContext(SeeTogetherContext);
  const { showToast } = useToast();
  const userToken = localStorage.getItem('userToken');

  const getSeeTogetherTopics = async () => {
    try {
      if (!userToken) return;

      const topics = await getApi<TopicCardProps[]>('/members/my/atlas');
      setSeeTogetherTopics(topics.map((topic) => topic.id));
    } catch {
      showToast(
        'error',
        '로그인 정보가 만료되었습니다. 로그아웃 후 다시 로그인 해주세요.',
      );
    }
  };

  useEffect(() => {
    getSeeTogetherTopics();
  }, []);

  if (!seeTogetherTopics) return null;
  if (seeTogetherTopics.length === 0) return null;

  return <Wrapper>{seeTogetherTopics.length}</Wrapper>;
}

const initAnimation = keyframes`
  0% {
    transform: translateY(0)
  }

  50% {
    transform: translateY(-20px)
  }

  100% {
    transform: translateY(0)
  }
`;

const Wrapper = styled.div`
  animation: ${initAnimation} ease 0.5s 1;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 24px;
  height: 24px;
  font-size: 12px;
  color: ${({ theme }) => theme.color.white};
  background-color: ${({ theme }) => theme.color.primary};
  border-radius: 50%;
  position: absolute;
  top: -12px;
  right: -4px;
  box-shadow: 0 4px 4px 0 rgba(0, 0, 0, 0.25);
`;

export default SeeTogetherCounter;
