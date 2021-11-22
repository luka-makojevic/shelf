import React, { useState } from 'react'
import { Error, Input, InputContainer, SeenIcon } from './input-styles'

const InputField = ({
  name,
  placeholder,
  register,
  validationRule,
  errors,
  type,
}: any) => {
  const error = errors[name] && <Error>{errors[name].message}</Error>

  const [passwordShown, setPasswordShown] = useState(false)

  const togglePasswordVisiblity = () => {
    setPasswordShown(!passwordShown)
  }

  return (
    <InputContainer>
      <Input
        marginY={[1, 2]}
        padding={[1, 2]}
        name={name}
        placeholder={placeholder}
        {...register(name, validationRule)}
      />
      {type === 'password' && (
        <SeenIcon
          src={
            passwordShown
              ? './assets/icons/eyeclosed.png'
              : './assets/icons/eyeopen.png'
          }
          onClick={togglePasswordVisiblity}
        />
      )}
      {error}
    </InputContainer>
  )
}
export default InputField

// export const PasswordField = ({
//   name,
//   placeholder,
//   register,
//   validationRule,
//   errors,
// }: any) => {
//   const error = errors[name] && <Error>{errors[name].message}</Error>

//   const [passwordShown, setPasswordShown] = useState(false)

//   const togglePasswordVisiblity = () => {
//     setPasswordShown(!passwordShown)
//   }

//   return (
//     <PasswordContainer>
//       <Input
//         marginY={[1, 2]}
//         padding={[1, 2]}
//         name={name}
//         type={passwordShown ? 'password' : 'text'}
//         placeholder={placeholder}
//         {...register(name, validationRule)}
//       />
//       <SeenIcon
//         src={
//           passwordShown
//             ? './assets/icons/eyeclosed.png'
//             : './assets/icons/eyeopen.png'
//         }
//         onClick={togglePasswordVisiblity}
//       />
//       {error}
//     </PasswordContainer>
//   )
// }
