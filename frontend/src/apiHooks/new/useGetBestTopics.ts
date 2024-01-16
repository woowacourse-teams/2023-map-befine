import { useSuspenseQuery } from '@tanstack/react-query';

import { getBestTopics } from '../../apis/new';

const useGetBestTopics = () => {
  const { data: bestTopics } = useSuspenseQuery({
    queryKey: ['GetBestTopics'],
    queryFn: getBestTopics,
  });

  return { bestTopics };
};

export default useGetBestTopics;
