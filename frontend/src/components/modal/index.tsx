import ReactDOM from 'react-dom';
import { FaRegTimesCircle } from 'react-icons/fa';
import { theme } from '../../theme';
import { H2 } from '../text/text-styles';
import {
  Backdrop,
  ModalContainer,
  Header,
  Close,
  Body,
  HeaderItem,
} from './modal.styles';
import { ModalProps } from './modal.interfaces';

const Modal = ({ onCloseModal, children, title, closeIcon }: ModalProps) => {
  const handleCloseModal = () => {
    onCloseModal(false);
  };

  const modal = (
    <Backdrop>
      <ModalContainer>
        <Header>
          <HeaderItem>
            <H2>{title}</H2>
          </HeaderItem>

          {closeIcon && (
            <HeaderItem>
              <Close onClick={handleCloseModal}>
                <FaRegTimesCircle
                  color={theme.colors.primary}
                  size={theme.space.lg}
                />
              </Close>
            </HeaderItem>
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
