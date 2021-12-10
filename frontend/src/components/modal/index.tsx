import ReactDOM from 'react-dom';
import { FaRegTimesCircle } from 'react-icons/fa';
import { theme } from '../../theme';
import { Backdrop, ModalContainer, Header, Close, Body } from './modal-styles';
import { ModalProps } from './modal.interfaces';

const Modal = ({ onCloseModal, children, title, closeIcon }: ModalProps) => {
  const handleCloseModal = () => {
    onCloseModal(false);
  };

  const modal = (
    <Backdrop>
      <ModalContainer>
        <Header>
          {title}

          {closeIcon && (
            <Close onClick={handleCloseModal}>
              <FaRegTimesCircle
                color={theme.colors.primary}
                size={theme.space.lg}
              />
            </Close>
          )}
        </Header>
        <Body>{children}</Body>
      </ModalContainer>
    </Backdrop>
  );

  const modalPortal = document.getElementById('modal-root');
  return <>{modalPortal ? ReactDOM.createPortal(modal, modalPortal) : null}</>;
};

export default Modal;
