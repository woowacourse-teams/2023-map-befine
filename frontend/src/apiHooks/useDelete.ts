import { deleteApi } from '../apis/deleteApi';
import useToast from '../hooks/useToast';
import { ContentTypeType } from '../types/Api';

interface fetchDeleteProps {
  url: string;
  contentType?: ContentTypeType;
}

const useDelete = () => {
  const { showToast } = useToast();

  const fetchDelete = async (
    { url, contentType }: fetchDeleteProps,
    errorMessage: string,
    onSuccess: () => void,
  ) => {
    try {
      await deleteApi(url, contentType);

      if (onSuccess) {
        onSuccess();
      }
    } catch (e) {
      showToast('error', errorMessage);
    }
  };

  return { fetchDelete };
};

export default useDelete;
