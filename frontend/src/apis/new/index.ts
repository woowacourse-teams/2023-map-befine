import { ClusteredCoordinates } from '../../pages/SelectedTopic/types';
import { TopicCardProps, TopicDetailProps } from '../../types/Topic';
import { http } from './http';

export const getTopics = (url: string) => http.get<TopicCardProps[]>(url);

export const getProfile = () =>
  http.get<TopicCardProps[] | null>('/members/my/topics');

export const getTopicDetail = (topicId: string) =>
  http.get<TopicDetailProps[]>(`/topics/ids?ids=${topicId}`);

export const getClusteredCoordinates = (
  topicId: string,
  distanceOfPinSize: number,
) =>
  http.get<ClusteredCoordinates[]>(
    `/topics/clusters?ids=${topicId}&image-diameter=${distanceOfPinSize}`,
  );
