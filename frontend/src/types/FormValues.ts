export interface DefaultFormProps {
  name: string;
  address: string;
  description: string;
}

export interface NewTopicFormProps {
  name: string;
  description: string;
  image: string;
  topics: [];
}

export interface ModifyPinFormProps {
  name: string;
  description: string;
}

export interface DefaultPinFormProps extends ModifyPinFormProps {
  id: number;
  address: string;
  latitude: string;
  longitude: string;
  updatedAt: string;
}

export interface NewPinFormProps {
  topicId: number;
  name: string;
  images: string[];
  description: string;
  address: string;
  latitude: string;
  longitude: string;
  legalDongCode: string;
}
