import { useQuery } from '@tanstack/react-query';

import { getNewestTopics } from '../../apis/new';

const useGetNewestTopics = () => {
  const { isLoading, data: newestTopics } = useQuery({
    queryKey: ['NewestTopics'],
    queryFn: getNewestTopics,
  });

  return { isLoading, newestTopics };
};

export default useGetNewestTopics;
