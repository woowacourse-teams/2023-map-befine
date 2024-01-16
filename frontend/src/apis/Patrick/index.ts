import { http } from './http';
import { TopicCardProps } from '../../types/Topic';

export const getProfile = () => {
  return http.get<TopicCardProps[] | null>("/members/my/topics");
};
