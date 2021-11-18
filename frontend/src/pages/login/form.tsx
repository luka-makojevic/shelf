// import React, { useContext, useState } from 'react'

// export default function form(onSubmit) {
//   const [email, setEmail] = useState('')
//   const [password, setPassword] = useState('')
//   const handleChangeEmail = (event: React.FormEvent<HTMLInputElement>) => {
//     const newValue = event.currentTarget.value
//     setEmail(newValue)
//     console.log(email)
//   }
//   return (
//     <>
//       <Form>
//         <Form.Title>Login</Form.Title>
//         <Form.Base onSubmit={onSubmit}>
//           <Form.Input
//             placeholder="email"
//             type="email"
//             value={email}
//             onChange={handleChangeEmail}
//           />
//           <Form.Input
//             type="password"
//             value={password}
//             onChange={handleChangePassword}
//           />
//           <Form.Submit>Sign in</Form.Submit>
//         </Form.Base>
//         <Form.AccentText>
//           New to Shelf?
//           <Form.Link to="/register" onClick={handleSubmit}>
//             Sign Up
//           </Form.Link>
//         </Form.AccentText>
//       </Form>
//     </>
//   )
// }

export {}
