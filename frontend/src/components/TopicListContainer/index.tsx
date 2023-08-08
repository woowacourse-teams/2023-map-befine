import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Text from '../common/Text';
import TopicCardList from '../TopicCardList';

interface TopicListContainerProps {
  containerTitle: string;
  routeWhenSeeAll: () => void;
}

const TopicListContainer = ({
  containerTitle,
  routeWhenSeeAll,
}: TopicListContainerProps) => (
  <section>
    <Flex $justifyContent="space-between" $alignItems="baseline">
      <Text color="black" $fontSize="large" $fontWeight="bold" tabIndex={0}>
        {containerTitle}
      </Text>
      <PointerText
        color="gray"
        $fontSize="small"
        $fontWeight="normal"
        tabIndex={1}
        onClick={routeWhenSeeAll}
      >
        전체 보기
      </PointerText>
    </Flex>
    <TopicCardList />
  </section>
);

const PointerText = styled(Text)`
  cursor: pointer;
`;

export default TopicListContainer;
