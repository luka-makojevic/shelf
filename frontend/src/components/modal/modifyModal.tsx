import React from 'react';
import { useForm } from 'react-hook-form';
import Modal from '.';
import { ShelfFormData } from '../../interfaces/dataTypes';
import { whiteSpaceRegex } from '../../utils/validation/regex';
import { Base, InputFieldWrapper } from '../form/form-styles';
import { ModalButtonDivider } from '../layout/layout.styles';
import { Button } from '../UI/button';
import { InputField } from '../UI/input/InputField';
import { ModifyModalProps } from './modal.interfaces';

const makeDefaultValue = (defaultValue: string | undefined) => {
  if (defaultValue?.substring(0, defaultValue.lastIndexOf('.'))) {
    return defaultValue?.substring(0, defaultValue.lastIndexOf('.'));
  }
  return defaultValue;
};

const validations = {
  required: 'This field is required',
  maxLength: {
    value: 50,
    message: 'Name can not be longer than 50 characters',
  },
  pattern: {
    value: whiteSpaceRegex,
    message: 'Invalid name format',
  },
};

export const ModifyModal = ({
  onCloseModal,
  onSubmit,
  title,
  placeHolder,
  buttonMessage,
  defaultValue,
}: ModifyModalProps) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ShelfFormData>({
    defaultValues: {
      name: makeDefaultValue(defaultValue),
    },
  });

  return (
    <Modal onCloseModal={onCloseModal} title={title}>
      <Base onSubmit={handleSubmit(onSubmit)}>
        <InputFieldWrapper>
          <InputField
            placeholder={placeHolder}
            error={errors.name}
            {...register('name', validations)}
          />
        </InputFieldWrapper>

        <ModalButtonDivider>
          <Button type="button" variant="lightBordered" onClick={onCloseModal}>
            Cancel
          </Button>
          <Button>{buttonMessage}</Button>
        </ModalButtonDivider>
      </Base>
    </Modal>
  );
};
