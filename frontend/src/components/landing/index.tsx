import { Content, Left, Right, Top } from './landing-styles'

// to do (delete)

const Landing = ({ children, ...restProps }: any) => {
  return <div {...restProps}>{children}</div>
}

Landing.Top = function LandingTop({ children, ...restProps }: any) {
  return <Top {...restProps}>{children}</Top>
}

Landing.Content = function LandingContent({ children, ...restProps }: any) {
  return <Content {...restProps}>{children}</Content>
}

Landing.Left = function LandingLeft({ children, ...restProps }: any) {
  return <Left {...restProps}>{children}</Left>
}

Landing.Right = function LandingRight({ children, ...restProps }: any) {
  return <Right {...restProps}>{children}</Right>
}

export default Landing
