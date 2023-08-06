import Box from '../common/Box';
import Text from '../common/Text';

interface TagProps {
  children: string;
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
      <Text color="white" $fontSize="small" $fontWeight="normal">
        {children}
      </Text>
    </Box>
  );
};

export default Tag;
