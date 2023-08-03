// TmapTypes.ts
interface TmapPosition {
  _lat: number;
  _lng: number;
}

interface TmapMarkerData {
  options: TmapMarkerOptions;
}

interface TmapMarkerOptions {
  animation: string;
  id: string;
  zIndex: number;
}

interface TmapMarker {
  id: string;
  getPosition: () => Tmapv2['LatLng'];
  addListener: (eventName: string, eventHandler: () => void) => void;
  setMap: (map: Tmapv2['Map'] | null) => void;
  _marker_data: TmapMarkerData;
}

interface TmapEvent {
  latLng: TmapPosition;
}

interface Tmapv2 {
  LatLng: new (latitude: number, longitude: number) => Tmapv2['LatLng'];
  LatLngBounds: new () => Tmapv2['LatLngBounds'];
  Map: new (element: HTMLElement, options?: any) => Tmapv2['Map'];
  MarkerOptions: {
    ANIMATE_DROP: string;
  };
}

interface TmapMapOptions {
  minZoom: number;
  maxZoom: number;
  zoom: number;
  center: Tmapv2['LatLng'];
}
