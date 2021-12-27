import shelfServices from '../services/shelfServices';
import { setFiles } from '../store/fileReducer';
import { useAppDispatch } from '../store/hooks';
import { setLoading } from '../store/loadingReducer';
import { setPathHistory } from '../store/pathHistory';
import { setShelves } from '../store/shelfReducer';
import { setTrash } from '../store/trashReducer';

export const useShelf = () => {
  const dispatch = useAppDispatch();

  const getShelves = (
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    dispatch(setLoading(true));

    shelfServices
      .getShelves()
      .then((res) => {
        dispatch(setShelves(res.data));
        onSuccess();
      })
      .catch((err) => {
        onError(err.response?.data.message);
      })
      .finally(() => {
        dispatch(setLoading(false));
      });
  };

  const getTrash = (
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    dispatch(setLoading(true));
    shelfServices
      .getTrash()
      .then((res) => {
        dispatch(setTrash(res.data.shelfItems));
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

  const getTrashFolders = (
    id: number,
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    dispatch(setLoading(true));

    shelfServices
      .getTrashFiles(id)
      .then((res) => {
        dispatch(setTrash(res.data.shelfItems));
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

  return { getShelves, getTrash, getTrashFolders };
};
