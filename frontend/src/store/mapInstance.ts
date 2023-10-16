import { create } from 'zustand';

interface MapState {
  mapInstance: TMap | null;
  setMapInstance: (instance: TMap) => void;
}

const useMapStore = create<MapState>((set) => ({
  mapInstance: null,
  setMapInstance: (instance: TMap) => set(() => ({ mapInstance: instance })),
}));

export default useMapStore;
