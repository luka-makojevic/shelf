import { Feature } from '../components'
import { Title } from '../components/feature/feature-styles'

const HomeContainer = () => {
  return (
    <Feature>
      <Title fontSize={2}>Hello</Title>
      <Feature.SubTitle>
        Event-driven compute service that lets you run code for virtually any
        type of application or backend service without provisioning or managing
        servers.
      </Feature.SubTitle>
    </Feature>
  )
}

export default HomeContainer
