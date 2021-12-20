import fileServices from '../services/fileServices';
import { useAppDispatch } from '../store/hooks';
import { setLoading } from '../store/loadingReducer';
import { setTrash } from '../store/trashReducer';

export const useTrash = () => {
  const dispatch = useAppDispatch();

  const getTrash = (
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    dispatch(setLoading(true));
    fileServices
      .getTrashFiles()
      .then((res) => {
        dispatch(setTrash(res.data));
        onSuccess();
      })
      .catch((err) => {
        onError(err.response?.data.message);
      })
      .finally(() => {
        dispatch(setLoading(false));
      });
  };

  return { getTrash };
};
