import { useSuspenseQuery } from '@tanstack/react-query';

import { getProfile } from '../../apis/new';

const useProfileList = () => {
  const { data, refetch } = useSuspenseQuery({
    queryKey: ['profileList'],
    queryFn: getProfile,
  });

  return { data, refetch };
};

export default useProfileList;
