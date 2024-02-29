import { TopicCardProps, TopicDetailProps } from '../../types/Topic';
import { http } from './http';

export const getTopics = (url: string) => http.get<TopicCardProps[]>(url);

export const getProfile = () =>
  http.get<TopicCardProps[] | null>('/members/my/topics');

export const getTopicDetail = (topicId: string) =>
  http.get<TopicDetailProps[]>(`/topics/ids?ids=${topicId}`);
