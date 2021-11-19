import { Link } from 'react-router-dom'
import { Error } from '../../components/form/form-styles'
import { Routes } from '../../enums/routes'

const CheckBox = ({ register, error }: any) => {
  return (
    <>
      <div style={{ marginTop: '1em' }}>
        <p style={{ fontSize: '0.7em' }}>
          I accept{' '}
          <Link to={Routes.TERMS} target="_blank">
            Terms and Conditions
          </Link>
        </p>
        <input
          {...register('terms', { required: 'This is required' })}
          type="checkbox"
          name="terms"
          id="termsID"
        />
        <Error>{error}</Error>
      </div>
    </>
  )
}

export default CheckBox
