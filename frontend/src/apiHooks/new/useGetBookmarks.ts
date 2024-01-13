import { useQuery } from '@tanstack/react-query';

import { getBookmarks } from '../../apis/new';

const useGetBookmarks = () => {
  const { isLoading, data: bookmarks } = useQuery({
    queryKey: ['GetBookmarks'],
    queryFn: getBookmarks,
  });

  return { isLoading, bookmarks };
};

export default useGetBookmarks;
