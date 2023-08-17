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
    <Wrapper>
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
    </Wrapper>
  );
};

const Wrapper = styled.ul`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
`;

export default BookmarksList;
