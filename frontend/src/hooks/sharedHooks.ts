import { useDispatch } from 'react-redux';
import sharedServices from '../services/sharedServices';
import { setLoading } from '../store/loadingReducer';
import { setShared } from '../store/sharedReducer';

export const useShare = () => {
  const dispatch = useDispatch();

  const getShared = (onSuccess: () => void, onError: (err: string) => void) => {
    dispatch(setLoading(true));
    sharedServices
      .getSharedFiles()
      .then((res) => {
        dispatch(setShared(res.data));
        onSuccess();
      })
      .catch((err) => {
        onError(err.response?.data.message);
      })
      .finally(() => dispatch(setLoading(false)));
  };

  return { getShared };
};
