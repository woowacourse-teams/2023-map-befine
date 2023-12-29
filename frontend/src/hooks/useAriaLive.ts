import { useEffect, useState } from 'react';

interface Props {
  liveTagId: string;
  defaultAnnounceText: string;
}

const useAriaLive = ({ liveTagId = '', defaultAnnounceText = '' }: Props) => {
  const [announceText, setAnnounceText] = useState(defaultAnnounceText);

  const setAriaLiveTagInnerText = (text: string) => {
    setAnnounceText(text);
  };

  useEffect(() => {
    if (announceText) {
      const ariaLiveTag = document.getElementById(liveTagId);

      if (ariaLiveTag) {
        ariaLiveTag.innerText = announceText;
      }
    }
  }, [announceText]);

  return { setAriaLiveTagInnerText };
};

export default useAriaLive;
