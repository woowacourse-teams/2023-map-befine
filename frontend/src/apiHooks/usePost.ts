import { postApi } from '../apis/postApi';
import useToast from '../hooks/useToast';
import { ContentTypeType } from '../types/Api';

interface fetchPostProps {
  url: string;
  payload: {};
  contentType?: ContentTypeType;
}

const usePost = () => {
  const { showToast } = useToast();

  const fetchPost = async (
    { url, payload, contentType }: fetchPostProps,
    errorMessage: string,
    onSuccess?: () => void,
  ) => {
    try {
      const responseData = await postApi(url, payload, contentType);

      if (onSuccess) {
        onSuccess();
      }

      return responseData;
    } catch (e) {
      showToast('error', errorMessage);
    }
  };

  return { fetchPost };
};

export default usePost;
