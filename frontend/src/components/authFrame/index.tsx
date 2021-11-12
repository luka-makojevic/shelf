import { Inner,ContainerRight,ContainerLeft,Feature, Title, SubTitle } from "./authFrame-stlyes"

 const Frame = ({children, ...restProps}: any) => {
    return (
       <Inner {...restProps}>{children}</Inner>
    )
}
Frame.ContainerRight = function FrameContainerRight({ children, ...restProps }: any) {
   return <ContainerRight {...restProps}>{children}</ContainerRight>
 }

 Frame.ContainerLeft = function FrameContainerLeft({ children, ...restProps }: any) {
   return <ContainerLeft {...restProps}>{children}</ContainerLeft>
 }
 Frame.Feature = function FrameFeature({ children, ...restProps }: any) {
   return <Feature {...restProps}>{children}</Feature>
 }
 Frame.Title = function FrameTitle({ children, ...restProps }: any) {
   return <Title {...restProps}>{children}</Title>
 }
  Frame.SubTitle = function FrameSubTitle({ children, ...restProps }: any) {
   return <SubTitle {...restProps}>{children}</SubTitle>
 }


export default Frame
