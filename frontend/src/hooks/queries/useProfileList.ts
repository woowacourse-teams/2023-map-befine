import { useQuery } from '@tanstack/react-query';
import { getProfile } from '../../apis/Patrick';
import { TopicCardProps } from '../../types/Topic';

const useProfileList = (url: string) => {
  return useQuery({
    queryKey: ['profileList'],
    queryFn: async () => {
      const data = await getProfile(url);
      return data;
    },
  });
};

export default useProfileList;
