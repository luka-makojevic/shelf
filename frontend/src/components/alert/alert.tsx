import { useEffect } from 'react';
import ReactDOM from 'react-dom';
import {
  AiOutlineCloseCircle,
  AiOutlineCheckCircle,
  AiOutlineInfoCircle,
} from 'react-icons/ai';
import { AlertMessage } from '../../enums/alertMessages';
import { AlertPorps } from '../../interfaces/types';
import { theme } from '../../theme';
import {
  AlertContainer,
  Close,
  Message,
  Title,
  ContentContainer,
  CloseContainer,
} from './alert-styles';

const alertPortal = document.getElementById('alert-root');

const AlertPortal = ({ type, title, message, onClose }: AlertPorps) => {
  const alert = (
    <AlertContainer type={type}>
      <CloseContainer>
        <Close onClick={onClose}>
          {type === AlertMessage.ERRROR && (
            <AiOutlineCloseCircle
              size={theme.space[5]}
              color={theme.colors.danger}
            />
          )}
          {type === AlertMessage.SUCCESS && (
            <AiOutlineCheckCircle
              size={theme.space[5]}
              color={theme.colors.success}
            />
          )}
          {type === AlertMessage.INFO && (
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
    </AlertContainer>
  );

  useEffect(() => {
    setTimeout(() => {
      onClose();
    }, 4000);
  }, []);

  return <>{alertPortal ? ReactDOM.createPortal(alert, alertPortal) : null}</>;
};

export default AlertPortal;
