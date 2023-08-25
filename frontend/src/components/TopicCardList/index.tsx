import { Fragment, useContext, useEffect, useState } from 'react';
import { getApi } from '../../apis/getApi';
import { TopicCardProps } from '../../types/Topic';
import TopicCard from '../TopicCard';
import { MarkerContext } from '../../context/MarkerContext';
import Flex from '../common/Flex';

interface TopicCardList {
  topics: TopicCardProps[];
  setTopicsFromServer: () => void;
}

const TopicCardList = ({ topics, setTopicsFromServer }: TopicCardList) => {
  const { markers, removeMarkers, removeInfowindows } =
    useContext(MarkerContext);

  useEffect(() => {
    if (markers.length > 0) {
      removeMarkers();
      removeInfowindows();
    }
  }, []);

  return (
    <ul>
      <Flex height="300px" $flexWrap="wrap" $gap="20px" overflow="hidden">
        {topics &&
          topics.map((topic, index) => {
            return (
              index < 6 && (
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
              )
            );
          })}
      </Flex>
    </ul>
  );
};

export default TopicCardList;
