import { ChangeEventHandler, useState } from 'react';
import { FieldError } from 'react-hook-form';
import { Error, Input, InputContainer, SeenIcon } from './input-styles';

export type InputFieldType = 'text' | 'password' | 'email';

export type InputFieldProps = {
  name?: string;
  placeholder?: string;
  onChange?: ChangeEventHandler;
  error?: FieldError;
  type?: InputFieldType;
  value?: string;
};

export const InputField = ({
  error,
  type = 'text',
  ...restProps
}: InputFieldProps) => {
  const [passwordShown, setPasswordShown] = useState(false);

  const handlePasswordVisibilityClick = () => {
    setPasswordShown(!passwordShown);
  };

  // console.log({ ...register(name, validationRule) })

  return (
    <InputContainer>
      <Input
        marginY={[1, 2]}
        padding={[1, 2]}
        type={!passwordShown ? type : 'text'}
        {...restProps}
      />
      {type === 'password' && (
        <SeenIcon
          src={
            passwordShown
              ? './assets/icons/eyeclosed.png'
              : './assets/icons/eyeopen.png'
          }
          onClick={handlePasswordVisibilityClick}
        />
      )}
      {error && <Error>{error.message}</Error>}
    </InputContainer>
  );
};
