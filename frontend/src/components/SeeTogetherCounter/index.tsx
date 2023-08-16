import { useContext, useEffect } from 'react';
import { SeeTogetherContext } from '../../context/SeeTogetherContext';
import { keyframes, styled } from 'styled-components';
import { getApi } from '../../apis/getApi';
import { TopicType } from '../../types/Topic';
import useToast from '../../hooks/useToast';

const SeeTogetherCounter = () => {
  const { seeTogetherTopics, setSeeTogetherTopics } =
    useContext(SeeTogetherContext);
  const { showToast } = useToast();

  const getSeeTogetherTopics = async () => {
    try {
      const topics = await getApi<TopicType[]>('default', '/members/my/atlas');
      setSeeTogetherTopics(topics);
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

  if (seeTogetherTopics.length === 0) return <></>;

  return <Wrapper>{seeTogetherTopics.length}</Wrapper>;
};

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
