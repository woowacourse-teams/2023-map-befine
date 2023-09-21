import { deleteApi } from '../apis/deleteApi';
import useToast from '../hooks/useToast';
import { ContentTypeType } from '../types/Api';

interface fetchDeleteProps {
  url: string;
  errorMessage: string;
  contentType?: ContentTypeType;
  onSuccess?: () => void;
  isThrow?: boolean;
}

const useDelete = () => {
  const { showToast } = useToast();

  const fetchDelete = async ({
    url,
    contentType,
    errorMessage,
    onSuccess,
    isThrow,
  }: fetchDeleteProps) => {
    try {
      await deleteApi(url, contentType);

      if (onSuccess) {
        onSuccess();
      }
    } catch (e) {
      showToast('error', errorMessage);

      if (isThrow) throw e;
    }
  };

  return { fetchDelete };
};

export default useDelete;
