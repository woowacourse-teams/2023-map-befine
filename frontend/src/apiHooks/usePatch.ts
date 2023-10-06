import { patchApi } from '../apis/patchApi';
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

const usePatch = () => {
  const { showToast } = useToast();

  const fetchPatch = async ({
    url,
    payload,
    contentType,
    errorMessage,
    onSuccess,
    isThrow,
  }: fetchPutProps) => {
    try {
      const responseData = await patchApi(url, payload, contentType);

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

  return { fetchPatch };
};

export default usePatch;
