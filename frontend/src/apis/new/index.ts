import { TopicCardProps } from '../../types/Topic';
import { http } from './http';

export const getBookmarks = () =>
  http.get<TopicCardProps[]>('/members/my/bookmarks');

export const getNewestTopics = () =>
  http.get<TopicCardProps[]>('/topics/newest');

export const getAllTopics = () => http.get<TopicCardProps>('/topics');

export const getBestTopics = () => http.get('/topics/bests');
