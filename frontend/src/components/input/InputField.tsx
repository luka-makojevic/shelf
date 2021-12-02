import React, { ChangeEventHandler, ForwardedRef, useState } from 'react';
import { FieldError } from 'react-hook-form';
import { Input, InputContainer, SeenIcon } from './input-styles';
import { Error } from '../text/text-styles';
import { InputFieldType } from '../../interfaces/types';

export type InputFieldProps = {
  name?: string;
  placeholder?: string;
  onChange?: ChangeEventHandler;
  error?: FieldError;
  type?: InputFieldType;
  value?: string;
};

export const InputField = React.forwardRef(
  (
    { error, type = 'text', ...restProps }: InputFieldProps,
    ref: ForwardedRef<HTMLInputElement>
  ) => {
    const [passwordShown, setPasswordShown] = useState(false);

    const handlePasswordVisibilityClick = () => {
      setPasswordShown(!passwordShown);
    };

    return (
      <InputContainer>
        <Input type={!passwordShown ? type : 'text'} ref={ref} {...restProps} />
        {type === 'password' && (
          <SeenIcon
            src={
              passwordShown
                ? `${process.env.PUBLIC_URL}/assets/icons/eyeopen.png`
                : `${process.env.PUBLIC_URL}/assets/icons/eyeclosed.png`
            }
            onClick={handlePasswordVisibilityClick}
          />
        )}
        {error && <Error>{error.message}</Error>}
      </InputContainer>
    );
  }
);
