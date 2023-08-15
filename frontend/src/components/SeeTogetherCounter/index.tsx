import { useContext } from 'react';
import { SeeTogetherContext } from '../../context/SeeTogetherContext';
import { styled } from 'styled-components';

const SeeTogetherCounter = () => {
  const { seeTogetherTopics } = useContext(SeeTogetherContext);

  if (seeTogetherTopics.length === 0) return <></>;

  return <Wrapper>{seeTogetherTopics.length}</Wrapper>;
};

const Wrapper = styled.div`
  width: 24px;
  height: 24px;
  font-size: 12px;
`;

export default SeeTogetherCounter;
