import { useQuery } from '@tanstack/react-query';

import { getTopics } from '../../api';

const useTopicsQuery = (url: string) => {
  const { data: topics, refetch: refetchTopics } = useQuery({
    queryKey: ['topics', url],
    queryFn: () => getTopics(url),
  });
  return { topics, refetchTopics };
};

export default useTopicsQuery;
