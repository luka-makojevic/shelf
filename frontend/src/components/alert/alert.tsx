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
  let alert;

  if (type === 'error') {
    alert = (
      <AlertBox type={type}>
        <CloseContainer>
          <Close onClick={onClose}>
            <AiOutlineCloseCircle
              size={theme.space[5]}
              color={theme.colors.danger}
            />
          </Close>
        </CloseContainer>
        <ContentContainer>
          <Title>{title}</Title>
          <Message>{message}</Message>
        </ContentContainer>
      </AlertBox>
    );
  }

  if (type === 'success') {
    alert = (
      <AlertBox type={type}>
        <CloseContainer>
          <Close onClick={onClose}>
            <AiOutlineCheckCircle
              size={theme.space[5]}
              color={theme.colors.success}
            />
          </Close>
        </CloseContainer>
        <ContentContainer>
          <Title>{title}</Title>
          <Message>{message}</Message>
        </ContentContainer>
      </AlertBox>
    );
  }

  if (type === 'info') {
    alert = (
      <AlertBox type={type}>
        <CloseContainer>
          <Close onClick={onClose}>
            <AiOutlineInfoCircle
              size={theme.space[5]}
              color={theme.colors.info}
            />
          </Close>
        </CloseContainer>
        <ContentContainer>
          <Title>{title}</Title>
          <Message>{message}</Message>
        </ContentContainer>
      </AlertBox>
    );
  }

  useEffect(() => {
    setTimeout(() => {
      onClose();
    }, 4000);
  }, []);

  return <>{alertPortal ? ReactDOM.createPortal(alert, alertPortal) : null}</>;
};

export default AlertContainer;
