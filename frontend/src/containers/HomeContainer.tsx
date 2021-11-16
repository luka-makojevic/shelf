import { Link } from 'react-router-dom'
import { Landing } from '../components'
import Card from '../components/card'

// to do
const HomeContainer = () => {
  const featuresInfo = [
    {
      img: './assets/images/cloud.png',
      text: 'Store and access your data from cloud',
    },
    {
      img: './assets/images/share.png',
      text: 'Share your files and have access to files that are shared with you',
    },
    {
      img: './assets/images/shelf.png',
      text: 'Built to store and retrive any amount of data from anywhere',
    },
    {
      img: './assets/images/f.png',
      text: 'Event-driven compute service that lets you run code for virtually any type of application or backend service without provisioning or managing servers',
    },
  ]

  return (
    <Landing>
      <Landing.Top>
        <Link to="/login">Sign in</Link>
        <Link to="/register">Sign up</Link>
      </Landing.Top>
      <Landing.Content>
        <Landing.Left>
          <img src="./assets/images/logo.png" alt="Shelf Logo" />
          <h1>Welcome to Shelf Storage Service</h1>
          <p>
            Event-driven compute service that lets you run code for virtually
            any type of application or backend service without provisioning or
            managing servers
          </p>
          <p className="call-to-action">
            Start working more efficiently today,{' '}
            <Link to="/register">Sign up to get started</Link>
          </p>
        </Landing.Left>
        <Landing.Right>
          {featuresInfo.map((feature) => (
            <Card image={feature.img} text={feature.text} />
          ))}
        </Landing.Right>
      </Landing.Content>
    </Landing>
  )
}

export default HomeContainer
