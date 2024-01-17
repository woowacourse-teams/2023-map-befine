import { useSuspenseQuery } from '@tanstack/react-query';

import { getTopics } from '../../apis/new';

const useGetTopics = (url: string) => {
  const { data: topics, refetch } = useSuspenseQuery({
    queryKey: ['topics', url],
    queryFn: () => getTopics(url),
  });

  return { topics, refetch };
};

export default useGetTopics;
