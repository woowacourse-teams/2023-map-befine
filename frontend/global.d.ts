interface Window {
  Tmapv3: {
    Map: new (element: HTMLElement, options?: { center?: LatLng }) => Map;
    LatLng: new (lat: number, lng: number) => LatLng;
    LatLngBounds: new () => LatLngBounds;
    Marker: new (options?: MarkerOptions) => Marker;
    InfoWindow: new (options?: InfoWindowOptions) => InfoWindow;
  };
  daum: any;
}

interface Map {
  setZoomLimit(minZoom: number, maxZoom: number): void;
  destroy(): void;
  panTo(latLng: LatLng): void;
  fitBounds(bounds: LatLngBounds): void;
  setCenter(latLng: LatLng): void;
  setZoom(zoomLevel: number): void;
  on(eventType: string, callback: (evt: Event) => void): void;
  removeListener(eventType: string, callback: (evt: Event) => void): void;
}

interface LatLng {}

interface LatLngBounds {
  extend(latLng: LatLng): void;
}

interface Marker {
  position?: LatLng;
  icon?: string;
  map?: Map;
  id?: string;
  getPosition(): LatLng;
  on(eventType: string, callback: (evt: Event) => void): void;
  setMap(mapOrNull?: Map | null): void; // Add this line
}

interface Point {
  x: number;
  y: number;
}

interface InfoWindow {
  position?: LatLng;
  content?: string;
  offset?: Point;
  type?: number;
  map?: Map;
  setPosition(positionOrLatLng?: Position | LatLng): void;
  setContent(contentOrString?: Content | string): void;
  open(map?: Map, marker?: Marker, latlng?: LatLng): void;
  close(): void;
}
