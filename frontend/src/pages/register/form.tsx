import { Form } from '../../components/form';
import { Title } from '../../components/text/text-styles';
import FormValidation from './formValidation';

const RegisterForm = ({ loading, onSubmit }: any) => {
  return (
    <>
      <Form>
        <Title fontSize={[4, 5, 6]}>Register</Title>
        <FormValidation loading={loading} onSubmit={onSubmit} />
      </Form>
    </>
  );
};

export default RegisterForm;
