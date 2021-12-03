import { useEffect } from 'react';
import ReactDOM from 'react-dom';
import {
  AiOutlineCloseCircle,
  AiOutlineCheckCircle,
  AiOutlineInfoCircle,
} from 'react-icons/ai';
import { AlertPorps } from '../../interfaces/types';
import { theme } from '../../theme';
import {
  AlertBox,
  Close,
  Message,
  Title,
  ContentContainer,
  CloseContainer,
} from './alert-styles';

const alertPortal = document.getElementById('alert-root');

const AlertContainer = ({ type, title, message, onClose }: AlertPorps) => {
  const alert = (
    <AlertBox type={type}>
      <CloseContainer>
        <Close onClick={onClose}>
          {type === 'error' && (
            <AiOutlineCloseCircle
              size={theme.space[5]}
              color={theme.colors.danger}
            />
          )}
          {type === 'success' && (
            <AiOutlineCheckCircle
              size={theme.space[5]}
              color={theme.colors.success}
            />
          )}
          {type === 'info' && (
            <AiOutlineInfoCircle
              size={theme.space[5]}
              color={theme.colors.info}
            />
          )}
        </Close>
      </CloseContainer>
      <ContentContainer>
        <Title>{title}</Title>
        <Message>{message}</Message>
      </ContentContainer>
    </AlertBox>
  );

  useEffect(() => {
    setTimeout(() => {
      onClose();
    }, 4000);
  }, []);

  return <>{alertPortal ? ReactDOM.createPortal(alert, alertPortal) : null}</>;
};

export default AlertContainer;
