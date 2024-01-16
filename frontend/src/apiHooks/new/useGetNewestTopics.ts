import { useSuspenseQuery } from '@tanstack/react-query';

import { getNewestTopics } from '../../apis/new';

const useGetNewestTopics = () => {
  const { data: newestTopics } = useSuspenseQuery({
    queryKey: ['GetNewestTopics'],
    queryFn: getNewestTopics,
  });

  return { newestTopics };
};

export default useGetNewestTopics;
