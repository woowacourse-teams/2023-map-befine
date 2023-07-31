export interface DefaultFormValuesType {
  name: string;
  address: string;
  description: string;
}

export interface NewTopicFormValuesType {
  name: string;
  description: string;
  image: string;
  topics: [];
}

export interface ModifyPinFormValuesType {
  name: string;
  images: string[];
  description: string;
}

export interface DefaultPinValuesType extends ModifyPinFormValuesType {
  id: number;
  address: string;
  latitude: string;
  longitude: string;
  updatedAt: string;
}

export interface NewPinValuesType {
  topicId: number;
  name: string;
  images: string[];
  description: string;
  address: string;
  latitude: string;
  longitude: string;
  legalDongCode: string;
}
