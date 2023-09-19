import { postApi } from '../apis/postApi';
import useToast from '../hooks/useToast';
import { ContentTypeType } from '../types/Api';

interface fetchPostProps {
  url: string;
  payload: {};
  contentType?: ContentTypeType;
  errorMessage: string;
  onSuccess?: () => void;
  isThrow?: boolean;
}

const usePost = () => {
  const { showToast } = useToast();

  const fetchPost = async ({
    url,
    payload,
    contentType,
    errorMessage,
    onSuccess,
    isThrow,
  }: fetchPostProps) => {
    try {
      const responseData = await postApi(url, payload, contentType);

      if (onSuccess) {
        onSuccess();
      }

      return responseData;
    } catch (e) {
      showToast('error', errorMessage);

      if (isThrow) throw e;
    }
  };

  return { fetchPost };
};

export default usePost;
