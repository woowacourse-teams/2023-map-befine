export interface DefaultFormValuesType {
  name: string;
  address: string;
  description: string;
}

export type NewTopicFormValuesType = Omit<DefaultFormValuesType, 'address'>;
