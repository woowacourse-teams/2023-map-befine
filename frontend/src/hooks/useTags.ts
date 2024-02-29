import { useContext, useEffect } from 'react';

import { TagContext } from '../context/TagContext';
import useNavigator from './useNavigator';

interface Props {
  isInitTags: boolean;
}

const useTags = ({ isInitTags }: Props) => {
  const { tags, setTags } = useContext(TagContext);
  const { routePage } = useNavigator();

  const onClickCreateTopicWithTags = () => {
    routePage('/new-topic', tags.map((tag) => tag.id).join(','));
  };

  const onClickInitTags = () => {
    setTags([]);
  };

  useEffect(() => {
    if (isInitTags) return;

    setTags([]);
  }, []);

  return { tags, onClickInitTags, onClickCreateTopicWithTags };
};

export default useTags;
