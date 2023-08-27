import { putApi } from '../apis/putApi';
import useToast from '../hooks/useToast';
import { ContentTypeType } from '../types/Api';

interface fetchPutProps {
  url: string;
  payload: {};
  contentType?: ContentTypeType;
}

const usePut = () => {
  const { showToast } = useToast();

  const fetchPut = async (
    { url, payload, contentType }: fetchPutProps,
    errorMessage: string,
    successMessage?: string,
  ) => {
    try {
      const responseData = await putApi(url, payload, contentType);

      if (successMessage) {
        showToast('info', successMessage);
      }

      return responseData;
    } catch (e) {
      showToast('error', errorMessage);
    }
  };

  return { fetchPut };
};

export default usePut;
