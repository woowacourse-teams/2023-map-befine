import { useSuspenseQuery } from '@tanstack/react-query';

import { getBookmarks } from '../../apis/new';

const useGetBookmarks = () => {
  const { data: bookmarks } = useSuspenseQuery({
    queryKey: ['GetBookmarks'],
    queryFn: getBookmarks,
  });

  return { bookmarks };
};

export default useGetBookmarks;
