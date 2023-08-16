import { useContext } from 'react';
import { SeeTogetherContext } from '../../context/SeeTogetherContext';
import { keyframes, styled } from 'styled-components';

const SeeTogetherCounter = () => {
  const { seeTogetherTopics } = useContext(SeeTogetherContext);

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
