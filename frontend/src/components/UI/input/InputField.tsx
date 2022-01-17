import React, { ForwardedRef, useState } from 'react';
import { Input, InputContainer, SeenIcon } from './input-styles';
import { Error } from '../../text/text-styles';
import { InputFieldProps } from './input.interfaces';

export const InputField = React.forwardRef(
  (
    { error, type = 'text', disabled, ...restProps }: InputFieldProps,
    ref: ForwardedRef<HTMLInputElement>
  ) => {
    const [passwordShown, setPasswordShown] = useState(false);

    const handlePasswordVisibilityClick = () => {
      setPasswordShown(!passwordShown);
    };

    return (
      <InputContainer>
        <Input
          type={!passwordShown || disabled ? type : 'text'}
          ref={ref}
          disabled={disabled}
          // autoComplete="off"
          {...restProps}
        />
        {type === 'password' &&
          !disabled && ( // TODO - maybe solve diferently
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
