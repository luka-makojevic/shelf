import { useAppDispatch } from '../store/hooks';
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
      .finally(() => {});
  };

  const getFolderFiles = (
    id: number,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
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
      .finally(() => {});
  };

  return { getShelfFiles, getFolderFiles };
};
