import { Link } from 'react-router-dom';
import { Error } from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';

const CheckBox = ({ register, error }: any) => (
  <div style={{ marginTop: '1em' }}>
    <p style={{ fontSize: '0.7em' }}>
      I accept{' '}
      <Link to={Routes.TERMS_AND_CONDITIONS} target="_blank">
        Terms and Conditions
      </Link>
    </p>
    <input
      {...register('terms', { required: 'This is required' })}
      // ref={register}
      type="checkbox"
      name="terms"
      id="termsID"
    />
    <Error>{error}</Error>
  </div>
);

export default CheckBox;
