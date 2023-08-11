import { styled } from 'styled-components';
import Text from '../common/Text';
import Flex from '../common/Flex';
import { postApi } from '../../apis/postApi';
import { getApi } from '../../apis/getApi';
import { MouseEventHandler, useContext } from 'react';
import { SeeTogetherContext } from '../../context/SeeTogetherContext';
import Button from '../common/Button';

const SeeTogetherButton = () => {
  const { setSeeTogetherTopic } = useContext(SeeTogetherContext);

  const onClickWatchGather = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();

    // await postApi('', {});
    // const data = await getApi('default', '');

    // setSeeTogetherTopic((prev) => [...prev, ...data]);
  };

  return (
    <CircleButton onClick={onClickWatchGather}>
      <Text color="white" $fontSize="extraLarge" $fontWeight="normal">
        +
      </Text>
    </CircleButton>
  );
};

const CircleButton = styled.button`
  width: 32px;
  height: 32px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: ${({ theme }) => theme.color.primary};
  border: 0;
  border-radius: 50%;
  cursor: pointer;
  box-sizing: border-box;
  font-size: 14px;
  box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.5);

  &:hover {
    filter: brightness(0.9);
  }
`;

export default SeeTogetherButton;
