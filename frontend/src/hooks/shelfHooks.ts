import shelfServices from '../services/shelfServices';
import { useAppDispatch } from '../store/hooks';
import { setLoading } from '../store/loadingReducer';
import { setShelves } from '../store/shelfReducer';

export const useShelf = () => {
  const dispatch = useAppDispatch();

  const getShelves = (
    data: {},
    onSuccess: () => void,
    onError: (error: string) => void
  ) => {
    dispatch(setLoading(true));

    shelfServices
      .getShelves()
      .then((res: any) => {
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

  return { getShelves };
};
