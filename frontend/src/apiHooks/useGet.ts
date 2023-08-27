import { getApi } from '../apis/getApi';
import useToast from '../hooks/useToast';

const useGet = () => {
  const { showToast } = useToast();

  const fetchGet = async <T>(
    url: string,
    errorMessage: string,
    onSuccess: (responseData: T) => void,
  ) => {
    try {
      const responseData = await getApi<T>(url);
      onSuccess(responseData);
    } catch (e) {
      showToast('error', errorMessage);
    }
  };

  return { fetchGet };
};

export default useGet;
