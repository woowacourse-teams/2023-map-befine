import { TopicCardProps } from '../../types/Topic';
import { http } from './http';

export const getTopics = (url: string) => http.get<TopicCardProps[]>(url);

export const getProfile = () =>
  http.get<TopicCardProps[] | null>('/members/my/topics');
