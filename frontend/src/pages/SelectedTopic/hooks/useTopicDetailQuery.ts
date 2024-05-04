import { useSuspenseQuery } from '@tanstack/react-query';

import { getTopicDetail } from '../../../apis/new';

const useTopicDetailQuery = (topicId: string) => {
  const { data, refetch, ...rest } = useSuspenseQuery({
    queryKey: ['topicDetail', topicId],
    queryFn: () => getTopicDetail(topicId),
    select: (response) => response[0],
  });

  return { data, refetch, ...rest };
};

export default useTopicDetailQuery;
