import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
  Container,
  EmailVerificationContainer,
} from '../../components/layout/layout.styles';
import userServices from '../../services/userServices';
import { Spinner } from '../../components/form/form-styles';
import { H2 } from '../../components/text/text-styles';
import { Routes } from '../../utils/enums/routes';
import { Button } from '../../components/UI/button';

const EmailVerification = () => {
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [confirmMessage, setConfirmMessage] = useState<string>('');
  const [resendMessage, setResendMessage] = useState<string>('');
  const [error, setError] = useState<string>('');
  const { token } = useParams();

  const resendEmailVerification = () => {
    userServices
      .resendEmailVerification(token)
      .then(() => {
        setResendMessage('Email was sent check, your inbox');
      })
      .catch((err) => setError(err.response?.data?.message));
  };

  useEffect(() => {
    setIsLoading(true);
    userServices
      .emailConfirmation(token)
      .then((res) => {
        setConfirmMessage(res.data.message);
      })
      .catch((err) => {
        setError(err.response?.data?.message);
      })
      .finally(() => setIsLoading(false));
  }, []);

  const feedbackMessage = () => {
    if (isLoading) {
      return (
        <Spinner src={`${process.env.PUBLIC_URL}/assets/icons/loading.png`} />
      );
    }
    if (resendMessage) {
      return <H2>{resendMessage}</H2>;
    }
    if (confirmMessage) {
      return (
        <EmailVerificationContainer>
          <H2>{confirmMessage}</H2>

          <Button variant="light" to={Routes.LOGIN}>
            Procced to login
          </Button>
        </EmailVerificationContainer>
      );
    }
    if (error === 'Token expired.') {
      return (
        <EmailVerificationContainer>
          <H2>{error}</H2>
          <Button onClick={resendEmailVerification} variant="light">
            Reset Token
          </Button>
        </EmailVerificationContainer>
      );
    }
    if (error === 'Token not valid.') {
      return (
        <EmailVerificationContainer>
          <H2>{error}</H2>
        </EmailVerificationContainer>
      );
    }
    return null;
  };

  return <Container>{feedbackMessage()}</Container>;
};

export default EmailVerification;
