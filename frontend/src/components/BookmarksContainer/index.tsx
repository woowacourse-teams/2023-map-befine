import { styled } from 'styled-components';
import Flex from '../common/Flex';
import Text from '../common/Text';
import Box from '../common/Box';
import Space from '../common/Space';
import { Suspense } from 'react';
import TopicCardListSeleton from '../TopicCardList/TopicCardListSeleton';
import BookmarksList from './BookmarksList';

interface BookmarksContainerProps {
  containerTitle: string;
  containerDescription: string;
}

const BookmarksContainer = ({
  containerTitle,
  containerDescription,
}: BookmarksContainerProps) => (
  <section>
    <Flex $justifyContent="space-between" $alignItems="flex-end">
      <Box>
        <Text
          color="black"
          $fontSize="extraLarge"
          $fontWeight="bold"
          tabIndex={0}
        >
          {containerTitle}
        </Text>
        <Space size={0} />
        <Text
          color="gray"
          $fontSize="default"
          $fontWeight="normal"
          tabIndex={1}
        >
          {containerDescription}
        </Text>
      </Box>

      <PointerText
        color="primary"
        $fontSize="small"
        $fontWeight="normal"
        tabIndex={2}
      >
        생선된 순
      </PointerText>
    </Flex>

    <Space size={6} />

    <Suspense fallback={<TopicCardListSeleton />}>
      <BookmarksList />
    </Suspense>
  </section>
);

const PointerText = styled(Text)`
  cursor: pointer;
`;

export default BookmarksContainer;
