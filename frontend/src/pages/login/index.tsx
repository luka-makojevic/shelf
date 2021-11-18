import React, { useState } from 'react'
import Header from '../../components/header'
import Form from '../../components/form'
import { AuthContext } from '../../providers/authProvider'

const Login = () => {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')

  // const handleChangeEmail = (event: React.FormEvent<HTMLInputElement>) => {
  //   const newValue = event.currentTarget.value
  //   setEmail(newValue)
  //   console.log(email)
  // }

  // const handleChangePassword = (event: React.FormEvent<HTMLInputElement>) => {
  //   const newValue = event.currentTarget.value
  //   setPassword(newValue)
  //   console.log(password)
  // }

  // const handleSubmit = (event: React.MouseEvent) => {
  //   event.preventDefault()
  //   login(data,(succes)=>{},(err:string)=>{setError(err)})
  // }

  return (
    <div>
      {/* {<Header profile={false} />
      <Frame>
        <Frame.ContainerRight>
         <Form/>
        </Frame.ContainerRight>
        <Frame.ContainerLeft>
          <Frame.Feature>
            <Frame.Title>Welcome back</Frame.Title>
            <Frame.SubTitle>
              To keep connected with us, please sign in with your personal info
            </Frame.SubTitle>
          </Frame.Feature>
        </Frame.ContainerLeft>
      </Frame>
    </>} */}
    </div>
  )
}

export { Login }
