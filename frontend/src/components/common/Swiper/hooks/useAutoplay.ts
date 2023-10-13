import { useEffect, useRef, useState } from 'react';

interface Props {
  autoplay: boolean;
  $autoplayTime: number;
  childrenListLength: number;
  pos: number;
  setPos: React.Dispatch<React.SetStateAction<number>>;
}

const useAutoplay = ({
  autoplay,
  $autoplayTime,
  childrenListLength,
  pos,
  setPos,
}: Props) => {
  const [isPlaying, setIsPlaying] = useState<boolean>(autoplay);
  const intervalId = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    if (childrenListLength < 2) setIsPlaying(false);

    if (isPlaying) {
      intervalId.current = setInterval(
        () => {
          pos <= childrenListLength - 2
            ? setPos((prev) => prev + 1)
            : setPos(0);
        },
        $autoplayTime < 1000 ? 1000 : $autoplayTime,
      );
    }

    if (!isPlaying && intervalId.current) clearInterval(intervalId.current);

    return () => {
      if (intervalId.current) clearInterval(intervalId.current);
    };
  }, [childrenListLength, pos, setPos, $autoplayTime, isPlaying]);

  const toggleAutoplay = () => {
    setIsPlaying((prev) => !prev);
  };

  return {
    isPlaying,
    toggleAutoplay,
  };
};

export default useAutoplay;
