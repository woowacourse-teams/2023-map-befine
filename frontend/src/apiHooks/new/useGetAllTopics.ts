import { useQuery } from '@tanstack/react-query';

import { getAllTopics } from '../../apis/new';

const useGetAllTopics = () => {
  const { isLoading, data: allTopics } = useQuery({
    queryKey: ['GetAllTopics'],
    queryFn: getAllTopics,
  });

  return { isLoading, allTopics };
};

export default useGetAllTopics;
