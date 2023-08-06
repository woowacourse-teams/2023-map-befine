import { ReactNode } from 'react';
import { styled } from 'styled-components';
import Box from '../common/Box';
import Text from '../common/Text';

interface TagProps {
  children: string;
}

const Tag = ({ children }: TagProps) => {
  return (
    <Box
      height="36px"
      padding="8px 16px"
      $backgroundColor="black"
      $borderRadius="medium"
    >
      <Text color="white" $fontSize="small" $fontWeight="normal">
        {children}
      </Text>
    </Box>
  );
};

export default Tag;
