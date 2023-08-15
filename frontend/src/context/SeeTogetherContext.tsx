import {
  Dispatch,
  ReactNode,
  SetStateAction,
  createContext,
  useState,
} from 'react';
import { TopicType } from '../types/Topic';

interface SeeTogetherContextProps {
  seeTogetherTopics: TopicType[];
  setSeeTogetherTopics: Dispatch<SetStateAction<TopicType[]>>;
}

interface SeeTogetherProviderProps {
  children: ReactNode;
}

export const SeeTogetherContext = createContext<SeeTogetherContextProps>({
  seeTogetherTopics: [],
  setSeeTogetherTopics: () => {},
});

const SeeTogetherProvider = ({ children }: SeeTogetherProviderProps) => {
  const [seeTogetherTopics, setSeeTogetherTopics] = useState<TopicType[]>([
    {
      id: 1,
      name: '준팍의 또 토픽',
      image: 'https://map-befine-official.github.io/favicon.png',
      pinCount: 3,
      updatedAt: '2023-08-14T13:15:52.081527138',
    },
    {
      id: 2,
      name: '준팍의 두번째 토픽',
      image: 'https://map-befine-official.github.io/favicon.png',
      pinCount: 5,
      updatedAt: '2023-08-14T13:15:52.081536838',
    },
  ]);

  return (
    <SeeTogetherContext.Provider
      value={{
        seeTogetherTopics,
        setSeeTogetherTopics,
      }}
    >
      {children}
    </SeeTogetherContext.Provider>
  );
};

export default SeeTogetherProvider;
