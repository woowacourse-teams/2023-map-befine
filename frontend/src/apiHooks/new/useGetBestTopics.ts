import { useQuery } from '@tanstack/react-query';

import { getBestTopics } from '../../apis/new';

const useGetBestTopics = () => {
  const { isLoading, data: bestTopics } = useQuery({
    queryKey: ['GetBestTopics'],
    queryFn: getBestTopics,
  });

  return { isLoading, bestTopics };
};

export default useGetBestTopics;
