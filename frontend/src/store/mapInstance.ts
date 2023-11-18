import { create } from 'zustand';

interface MapContext {
  mapInstance: TMap | null;
  setMapInstance: (instance: TMap) => void;
}

const useMapStore = create<MapContext>((set) => ({
  mapInstance: null,
  setMapInstance: (instance: TMap) => set(() => ({ mapInstance: instance })),
}));

export default useMapStore;
