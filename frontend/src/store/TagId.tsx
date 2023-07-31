import { createContext, useState } from 'react';

export interface TagIdContextProps {
  tagId: number[];
  setTagId: (value: number[]) => void;
}

export const TagIdContext = createContext<TagIdContextProps | null>(null);

const TagContextProvider = (props: { children: React.ReactNode }) => {
  const [tagId, setTagId] = useState<number[]>([]);

  const contextValue: TagIdContextProps = {
    tagId: tagId,
    setTagId: setTagId,
  };

  return (
    <TagIdContext.Provider value={contextValue}>
      {props.children}
    </TagIdContext.Provider>
  );
};

export default TagContextProvider;
