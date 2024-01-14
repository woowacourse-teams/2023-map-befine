import { http } from './http';
import { TopicCardProps } from '../../types/Topic';

export const getProfile = (url: string) => {
  return http.get<TopicCardProps[] | null>(url);
};
