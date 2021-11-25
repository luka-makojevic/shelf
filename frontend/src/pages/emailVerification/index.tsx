import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Container, Holder } from '../../components/layout/layout.styles';
import userServices from '../../services/userServices';
import { Spinner } from '../../components/form/form-styles';
import { Link, Title } from '../../components/text/text-styles';
import { Routes } from '../../enums/routes';

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
      .catch((err) => setError(err.response.data?.message));
  };

  useEffect(() => {
    setIsLoading(true);
    userServices
      .emailConfirmation(token)
      .then((res) => {
        setConfirmMessage(res.data.message);
      })
      .catch((err) => {
        setError(err.response.data?.message);
      })
      .finally(() => setIsLoading(false));
  }, []);

  const feedbackMessage = () => {
    if (isLoading) {
      return <Spinner src="../assets/icons/loading.png" />;
    }
    if (resendMessage) {
      return <Title fontSize="25px">{resendMessage}</Title>;
    }
    if (confirmMessage) {
      return (
        <>
          <Title textAlign="center" fontSize="25px">
            {confirmMessage}
          </Title>

          <Link
            to={Routes.LOGIN}
            p="5px 10px"
            bg="white"
            mb="20px"
            borderRadius="30px"
          >
            Procced to login
          </Link>
        </>
      );
    }
    if (error) {
      return (
        <>
          <Title fontSize="20px">{error}</Title>
          <button onClick={resendEmailVerification}>Reset token</button>
        </>
      );
    }
    return null;
  };

  return (
    <Holder
      display="flex"
      flexDirection="column"
      maxWidth={['300px', '500px']}
      margin="100px auto"
      height="300px"
      justifyContent="center"
      alignItems="center"
      bg="primary"
      color="white"
      borderRadius="30px"
    >
      <Container
        p="70px 10px"
        flexDirection="column"
        alignItems="center"
        justifyContent="space-between"
      >
        {feedbackMessage()}
      </Container>
    </Holder>
  );
};

export default EmailVerification;
