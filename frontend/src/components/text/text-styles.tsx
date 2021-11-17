import styled from 'styled-components'
import { space, typography, color } from 'styled-system'
import { Link as ReachRouterLink } from 'react-router-dom'
import { TextProps } from '../../interfaces/styles'

export const Title = styled.h1<TextProps>`
    ${space}
    ${typography}
    ${color}
`
export const SubTitle = styled.p<TextProps>`
    ${space}
    ${typography}
    ${color}
`
export const AccentText = styled.p<TextProps>`
    ${space}
    ${typography}
    ${color}
    color: ${(({theme}) => theme.colors.primary)};
`
export const Link = styled(ReachRouterLink)<TextProps>`
    ${space}
    ${typography}
    ${color}
    color: ${(({theme}) => theme.colors.primary)};
    text-decoration: none;
    font-weight: ${(({theme}) => theme.fontWeights.bold)} ;

`
