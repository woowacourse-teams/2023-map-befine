import { useRef } from "react";

const useKeyDown = <T extends HTMLElement>() => {
  const elementRef = useRef<T | null>(null);

  const onElementKeyDown = (e: React.KeyboardEvent<T>) => {
    if (e.keyCode === 13) {
      e.preventDefault();
      elementRef.current?.click();
    }
  };

  return {elementRef, onElementKeyDown}
};

export default useKeyDown;
