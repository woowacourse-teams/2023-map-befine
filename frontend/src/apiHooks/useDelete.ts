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
    successMessage?: string,
  ) => {
    try {
      await deleteApi(url, contentType);

      if (successMessage) {
        showToast('info', successMessage);
      }
    } catch (e) {
      showToast('error', errorMessage);
    }
  };

  return { fetchDelete };
};

export default useDelete;
