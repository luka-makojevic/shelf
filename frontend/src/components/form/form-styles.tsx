import styled from "styled-components";
import { Link as ReachRouterLink  } from "react-router-dom"


export const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    min-height: 550px;
    max-width: 400px;
    padding: 10px 50px;
    
    border-radius: 50px;
    border: 2px solid  #8EA2D8;;
    width: 100%;
    margin: 30px auto 90px;
   
`

export const Base = styled.form`
    display: flex;
    flex-wrap: wrap;
    `

export const Title = styled.h1``

export const Error = styled.div``

export const Input = styled.input`
    width: 100%;
    margin: 10px 0;
    border: 1px solid #8EA2D8;
    border-radius: 15px;
    padding: 10px;

`

export const InputControl = styled(Input)`
    
`

export const SeenIcon = styled.img``

export const AccentText = styled.p`
    color:#006FD1;

`

export const Link = styled(ReachRouterLink)`
    text-decoration: none;
    color: inherit;
    font-weight: bold;
`

export const Submit = styled.button`
    width: 100%;
    margin: 10px 0;
    border: none;
    background: #006FD1 ;
    padding: 10px 20px;
    font-weight: 700;
    color: white;
    border-radius: 15px;
`
