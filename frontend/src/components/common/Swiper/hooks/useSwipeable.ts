import { TouchEventHandler, useRef, useState } from 'react';

interface Props {
  childrenListLength: number;
  pos: number;
  setPos: React.Dispatch<React.SetStateAction<number>>;
}

let isAcceleratingPos = false;
const CAN_SWIPE = 10;
const OPPOSITE_DIRECTION_CAN_SWIPE = 4;

const useSwipeable = ({ childrenListLength, pos, setPos }: Props) => {
  const [prevTouch, setPrevTouch] = useState<React.Touch | null>(null);
  const timerId = useRef<NodeJS.Timeout | null>(null);

  const acceleratePos = (diff: number) => {
    if (isAcceleratingPos) return;

    if (pos < childrenListLength - 1 && diff < -CAN_SWIPE) {
      isAcceleratingPos = true;
      setPos(pos + 1);
    }

    if (pos > 0 && diff > CAN_SWIPE) {
      isAcceleratingPos = true;
      setPos(pos - 1);
    }

    if (timerId.current) return;

    timerId.current = setTimeout(() => {
      isAcceleratingPos = false;

      if (timerId.current) {
        clearTimeout(timerId.current);
        timerId.current = null;
      }
    }, 150);
  };

  const increasePos = () => {
    if (pos < childrenListLength - 1) setPos(pos + 1);
  };

  const decreasePos = () => {
    if (pos > 0) setPos(pos - 1);
  };

  const moveToSettedPos = (settedPos: number) => {
    setPos(settedPos);
  };

  const handleTouchMove: TouchEventHandler = (event) => {
    const touch = event.touches[0]!;

    setPrevTouch(touch);
    if (!prevTouch) return;

    const diff = touch.pageX - prevTouch.pageX;
    const otherPos = touch.pageY - prevTouch.pageY;

    if (Math.abs(otherPos) > OPPOSITE_DIRECTION_CAN_SWIPE) return;

    acceleratePos(diff);
  };

  const handleTouchEnd: TouchEventHandler = () => {
    setPrevTouch(null);
  };

  return {
    pos,
    increasePos,
    decreasePos,
    moveToSettedPos,
    handleTouchMove,
    handleTouchEnd,
  };
};

export default useSwipeable;
