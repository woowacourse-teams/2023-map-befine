import { putApi } from '../apis/putApi';
import useToast from '../hooks/useToast';
import { ContentTypeType } from '../types/Api';

interface fetchPutProps {
  url: string;
  payload: {};
  errorMessage: string;
  contentType?: ContentTypeType;
  onSuccess?: () => void;
  isThrow?: boolean;
}

const usePut = () => {
  const { showToast } = useToast();

  const fetchPut = async ({
    url,
    payload,
    contentType,
    errorMessage,
    onSuccess,
    isThrow,
  }: fetchPutProps) => {
    try {
      const responseData = await putApi(url, payload, contentType);

      if (onSuccess) {
        onSuccess();
      }

      return responseData;
    } catch (e) {
      showToast('error', errorMessage);

      if (isThrow) throw e;
    }

    return null;
  };

  return { fetchPut };
};

export default usePut;
