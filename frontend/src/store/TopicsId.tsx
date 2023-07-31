import { createContext, useState } from 'react';

export interface TopicsIdContextProps {
  topicsId: number[];
  setTopicsId: (value: number[]) => void;
}

export const TopicsIdContext = createContext<TopicsIdContextProps | null>(null);

const TopicsIdContextProvider = (props: { children: React.ReactNode }) => {
  const [tagId, setTagId] = useState<number[]>([]);

  const contextValue: TopicsIdContextProps = {
    topicsId: tagId,
    setTopicsId: setTagId,
  };

  return (
    <TopicsIdContext.Provider value={contextValue}>
      {props.children}
    </TopicsIdContext.Provider>
  );
};

export default TopicsIdContextProvider;
