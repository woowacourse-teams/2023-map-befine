export interface ClusteredCoordinates {
  latitude: number;
  longitude: number;
  pins: ClusteredPin[];
}

interface ClusteredPin {
  id: number;
  name: string;
  topicId: number;
}
