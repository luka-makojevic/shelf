import React from 'react';
import { Title, Container, Logo,SubTitle  } from './feature-styles';

export default function Feature({ children, ...restProps }: any) {
  return <Container {...restProps}>{children}</Container>;
}

Feature.Title = function FeatureTitle({ children, ...restProps }: any) {
  return <Title {...restProps}>{children}</Title>;
};
Feature.SubTitle = function FeatureSubTitle({ children, ...restProps }: any) {
  return <SubTitle {...restProps}>{children}</SubTitle>;
};
Feature.Logo = function FeatureLogo({  ...restProps }: any) {
  return <Logo {...restProps}/>
};