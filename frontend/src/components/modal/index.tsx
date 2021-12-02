import ReactDOM from 'react-dom';
import { Backdrop, ModalContainer } from './modal-styles';

const Modal = () => {
  const modal = (
    <Backdrop>
      <ModalContainer>Modal</ModalContainer>
    </Backdrop>
  );

  const modalPortal = document.getElementById('modal-root');
  return <>{modalPortal ? ReactDOM.createPortal(modal, modalPortal) : null}</>;
};

export default Modal;
