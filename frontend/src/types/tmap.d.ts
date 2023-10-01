interface Window {
  Tmapv3: {
    Map: new (
      element: HTMLElement,
      options?: { center?: LatLng; scaleBar: boolean },
    ) => TMap;
    LatLng: new (lat: number, lng: number) => LatLng;
    LatLngBounds: new () => LatLngBounds;
    Marker: new (options?: MarkerOptions) => Marker;
    InfoWindow: new (options?: InfoWindowOptions) => InfoWindow;
    Point: new (x: number, y: number) => Point;
  };
  daum: any;
}

interface evt {
  data: {
    lngLat: {
      _lat: number;
      _lng: number;
    };
  };
}

interface TMap {
  setZoomLimit(minZoom: number, maxZoom: number): void;
  destroy(): void;
  panTo(latLng: LatLng): void;
  fitBounds(bounds: LatLngBounds): void;
  setCenter(latLng: LatLng): void;
  setZoom(zoomLevel: number): void;
  on(eventType: string, callback: (evt: evt) => void): void;
  removeListener(eventType: string, callback: (evt: evt) => void): void;
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
  setMap(mapOrNull?: Map | null): void;
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
  setMap(mapOrNull?: Map | null): void;
  setPosition(positionOrLatLng?: Position | LatLng): void;
  setContent(contentOrString?: Content | string): void;
  open(map?: Map, marker?: Marker, latlng?: LatLng): void;
  close(): void;
}
