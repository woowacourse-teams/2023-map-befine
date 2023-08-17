import { Fragment, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import { getApi } from '../../apis/getApi';
import TopicCard from '../TopicCard';
import { TopicType } from '../../types/Topic';
import useToast from '../../hooks/useToast';

interface BookmarksListProps {
  bookmarks: TopicType[];
  setTopicsFromServer: () => void;
}

const BookmarksList = ({
  bookmarks,
  setTopicsFromServer,
}: BookmarksListProps) => {
  return (
    <BookMarksListWrapper>
      {bookmarks.map((topic) => (
        <Fragment key={topic.id}>
          <TopicCard
            id={topic.id}
            image={topic.image}
            name={topic.name}
            creator={topic.creator}
            updatedAt={topic.updatedAt}
            pinCount={topic.pinCount}
            bookmarkCount={topic.bookmarkCount}
            isInAtlas={topic.isInAtlas}
            isBookmarked={topic.isBookmarked}
            setTopicsFromServer={setTopicsFromServer}
          />
        </Fragment>
      ))}
    </BookMarksListWrapper>
  );
};

const BookMarksListWrapper = styled.ul`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
  grid-row-gap: ${({ theme }) => theme.spacing[5]};
`;

export default BookmarksList;
