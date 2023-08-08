import { styled } from 'styled-components';
import Text from '../common/Text';
import Flex from '../common/Flex';
import { postApi } from '../../apis/postApi';
import { getApi } from '../../apis/getApi';
import { MouseEventHandler, useContext } from 'react';
import { SeeTogetherContext } from '../../context/SeeTogetherContext';

const SeeTogetherButton = () => {
  const { setSeeTogetherTopic } = useContext(SeeTogetherContext);

  const onClickWatchGather = async (
    e: React.MouseEvent<HTMLDivElement, MouseEvent>,
  ) => {
    e.stopPropagation();

    // await postApi('', {});
    // const data = await getApi('default', '');

    // setSeeTogetherTopic((prev) => [...prev, ...data]);
  };

  return (
    <CircleFlex
      $justifyContent="center"
      $alignItems="flex-end"
      width="32px"
      height="32px"
      $backgroundColor="primary"
      position="absolute"
      top="50%"
      right="12px"
      onClick={onClickWatchGather}
    >
      <Text color="white" $fontSize="extraLarge" $fontWeight="normal">
        +
      </Text>
    </CircleFlex>
  );
};

const CircleFlex = styled(Flex)`
  border-radius: 50%;
`;

export default SeeTogetherButton;
