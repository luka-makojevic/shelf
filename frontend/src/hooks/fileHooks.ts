import { useAppDispatch } from '../store/hooks';
import { setLoading } from '../store/loadingReducer';
import { setFiles } from '../store/fileReducer';
import { setPathHistory } from '../store/pathHistory';
import fileServices from '../services/fileServices';

export const useFiles = () => {
  const dispatch = useAppDispatch();

  const getShelfFiles = (
    id: number,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    dispatch(setLoading(true));

    fileServices
      .getShelfFiles(id)
      .then((res) => {
        dispatch(setFiles(res.data.shelfItems));
        dispatch(setPathHistory(res.data.breadCrumbs));
        onSuccess();
      })
      .catch((err) => {
        onError(err.response?.data.message);
      })
      .finally(() => {
        dispatch(setLoading(false));
      });
  };

  const getFolderFiles = (
    id: number,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    dispatch(setLoading(true));

    fileServices
      .getFolderFiles(id)
      .then((res) => {
        dispatch(setFiles(res.data.shelfItems));
        dispatch(setPathHistory(res.data.breadCrumbs));
        onSuccess();
      })
      .catch((err) => {
        onError(err.response?.data.message);
      })
      .finally(() => {
        dispatch(setLoading(false));
      });
  };

  return { getShelfFiles, getFolderFiles };
};
