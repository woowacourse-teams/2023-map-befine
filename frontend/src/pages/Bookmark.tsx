import { styled } from 'styled-components';
import BookmarksContainer from '../components/BookmarksContainer';
import Box from '../components/common/Box';
import { FULLSCREEN } from '../constants';
import useSetLayoutWidth from '../hooks/useSetLayoutWidth';
import useSetNavbarHighlight from '../hooks/useSetNavbarHighlight';

const Bookmark = () => {
    const { width: _ } = useSetLayoutWidth(FULLSCREEN);
    const { navbarHighlights: __ } = useSetNavbarHighlight('favorite');
    
  return (
    <BookMarksWrapper>
      <BookmarksContainer
        containerTitle="즐겨찾기"
        containerDescription="즐겨찾기한 지도들을 한눈에 보세요"
      ></BookmarksContainer>
    </BookMarksWrapper>
  );
};

const BookMarksWrapper = styled(Box)`
  width: 70vw;
  margin: 0 auto;
`;

export default Bookmark;
