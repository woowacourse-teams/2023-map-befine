import { styled } from 'styled-components';

import Box from '../common/Box';
import Flex from '../common/Flex';
import Space from '../common/Space';
import Text from '../common/Text';

export interface PinCardProps {
  pinTitle: string;
  pinAddress: string;
  pinDescription: string;
}

function PinCard({ pinTitle, pinAddress, pinDescription }: PinCardProps) {
  return (
    <Wrapper>
      <Flex position="relative">
        <Box width="192px" padding={1}>
          <Box height="52px">
            <Text color="black" $fontSize="default" $fontWeight="bold">
              {pinTitle}
            </Text>
          </Box>

          <Space size={0} />

          <Text color="gray" $fontSize="small" $fontWeight="normal">
            {pinAddress}
          </Text>

          <Space size={0} />
          <Text color="gray" $fontSize="small" $fontWeight="normal">
            {pinDescription}
          </Text>
        </Box>
      </Flex>
    </Wrapper>
  );
}

const Wrapper = styled.li`
  width: 332px;
  height: 140px;
  cursor: pointer;
  border: 1px solid ${({ theme }) => theme.color.gray};
  border-radius: ${({ theme }) => theme.radius.small};

  margin: 0 auto;
`;

export default PinCard;
