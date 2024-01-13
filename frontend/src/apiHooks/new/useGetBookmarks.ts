import { useQuery } from '@tanstack/react-query';

import { getBookmarks } from '../../apis/new';

const useGetBookmarks = () => {
  const { data: bookmarks } = useQuery({
    queryKey: ['GetBookmarks'],
    queryFn: getBookmarks,
  });

  return bookmarks;
};

export default useGetBookmarks;
