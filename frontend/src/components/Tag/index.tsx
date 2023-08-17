import { styled } from 'styled-components';
import Box from '../common/Box';
import Text from '../common/Text';

interface TagProps {
  children: React.ReactNode;
  tabIndex?: number;
}

const Tag = ({ children, tabIndex }: TagProps) => {
  return (
    <Box
      height="36px"
      padding="8px 16px"
      $backgroundColor="black"
      $borderRadius="medium"
      tabIndex={tabIndex}
    >
      <EllipsisText color="white" $fontSize="small" $fontWeight="normal">
        {children}
      </EllipsisText>
    </Box>
  );
};

const EllipsisText = styled(Text)`
  width: 100%;
  display: -webkit-box;
  word-wrap: break-word;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  text-overflow: ellipsis;
  overflow: hidden;
`;

export default Tag;
