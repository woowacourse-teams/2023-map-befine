import { useQuery } from '@tanstack/react-query';
import { getProfile } from '../../apis/Patrick';
import { TopicCardProps } from '../../types/Topic';

const useProfileList = () => {
  return useQuery({
    queryKey: ['profileList'],
    queryFn: getProfile,
  });
};

export default useProfileList;
