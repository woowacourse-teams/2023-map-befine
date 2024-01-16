import { useSuspenseQuery } from '@tanstack/react-query';

import { getAllTopics } from '../../apis/new';

const useGetAllTopics = () => {
  const { data: allTopics } = useSuspenseQuery({
    queryKey: ['GetAllTopics'],
    queryFn: getAllTopics,
  });

  return { allTopics };
};

export default useGetAllTopics;
