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
    successMessage?: string,
  ) => {
    try {
      const responseData = await postApi(url, payload, contentType);

      if (successMessage) {
        showToast('info', successMessage);
      }

      return responseData;
    } catch (e) {
      showToast('error', errorMessage);
    }
  };

  return { fetchPost };
};

export default usePost;
