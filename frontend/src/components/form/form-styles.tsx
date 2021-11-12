import styled from 'styled-components'
import { Link as ReachRouterLink } from 'react-router-dom'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 400px;
  max-width: 400px;
  padding: 10px 50px;

  border-radius: 50px;
  border: 2px solid #8ea2d8;
  width: 100%;
  margin: 30px auto 90px;
`

export const Base = styled.form`
  display: flex;
  flex-wrap: wrap;
`

export const Title = styled.h1``

export const Error = styled.div`
    color: red;
    border-radius: 15px;
    padding: 5px 10px;
    font-size: 12px;
`

export const Input = styled.input`
  width: 100%;
  margin: 10px 0;
  border: 1px solid #8ea2d8;
  border-radius: 15px;
  padding: 10px;

  &:focus {
    outline: none;
    box-shadow: 0 0 6px 0 #8ea2d8;
  }
`


export const Spinner = styled.img`
    filter:  invert(1);
    width: 30px;
    height:30px;
    
    animation-name: spin;
    animation-duration: 1000ms;
    animation-iteration-count: infinite;
    animation-timing-function: linear;
    @-ms-keyframes spin {
      from {
        -ms-transform: rotate(0deg);
      }
      to {
        -ms-transform: rotate(360deg);
      }
    }
    @-moz-keyframes spin {
      from {
        -moz-transform: rotate(0deg);
      }
      to {
        -moz-transform: rotate(360deg);
      }
    }
    @-webkit-keyframes spin {
      from {
        -webkit-transform: rotate(0deg);
      }
      to {
        -webkit-transform: rotate(360deg);
      }
    }
    @keyframes spin {
      from {
        transform: rotate(0deg);
      }
      to {
        transform: rotate(360deg);
      }
    }
`
export const InputControl = styled.div`
    position: relative;
    width: 100%;
`

export const SeenIcon = styled.img`
    position: absolute;
    width: 20px;
    height: 20px;
    right: 10px;
    top: 21px;
`

export const AccentText = styled.p`
  color: #006fd1;
`

export const Link = styled(ReachRouterLink)`
  text-decoration: none;
  color: inherit;
  font-weight: bold;
`

export const Submit = styled.button`
    width: 100%;
    margin: 10px 0;
    min-height: 40px;
    border: none;
    background: #006FD1 ;
    padding: 0px 20px;
    font-weight: 700;
    color: white;
    border-radius: 15px;
`
