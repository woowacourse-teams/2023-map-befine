import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';

const useSetSelectedPinId = () => {
  const [searchParams, _] = useSearchParams();
  const [isDoubleSidebarOpen, setIsDoubleSidebarOpen] = useState(true);
  const [selectedPinId, setSelectedPinId] = useState<number | null>(null);

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);

    if (queryParams.has('pinDetail')) {
      setSelectedPinId(Number(queryParams.get('pinDetail')));
      setIsDoubleSidebarOpen(true);
      return;
    }

    setSelectedPinId(null);
  }, [searchParams]);

  return { isDoubleSidebarOpen, selectedPinId, setIsDoubleSidebarOpen, setSelectedPinId };
};

export default useSetSelectedPinId;
