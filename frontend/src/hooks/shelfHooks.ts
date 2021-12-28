import shelfServices from '../services/shelfServices';
import { useAppDispatch } from '../store/hooks';
import { setShelves } from '../store/shelfReducer';

export const useShelf = () => {
  const dispatch = useAppDispatch();

  const getShelves = (
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    shelfServices
      .getShelves()
      .then((res) => {
        dispatch(setShelves(res.data));
        onSuccess();
      })
      .catch((err) => {
        onError(err.response?.data.message);
      })
      .finally(() => {});
  };

  return { getShelves };
};
