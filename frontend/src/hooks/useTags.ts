import { useContext } from 'react';

import { TagContext } from '../context/TagContext';
import useNavigator from './useNavigator';

const useTags = () => {
  const { tags, setTags } = useContext(TagContext);
  const { routePage } = useNavigator();

  const onClickCreateTopicWithTags = () => {
    routePage('/new-topic', tags.map((tag) => tag.id).join(','));
  };

  const onClickInitTags = () => {
    setTags([]);
  };

  return { tags, setTags, onClickInitTags, onClickCreateTopicWithTags };
};

export default useTags;
