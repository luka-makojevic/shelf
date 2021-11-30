import { Wrapper } from '../../components/layout/layout.styles';
import { tos, TOStitle, TOSsubTitle } from '../../fixtures/termsOfService';
import TextCard from '../../components/textCard';

const TermsAndConditions = () => {
  const title = TOStitle;
  const subtitle = TOSsubTitle;
  const text = tos;
  return (
    <Wrapper justifyContent="center">
      <TextCard title={title} text={text} subtitle={subtitle} />
    </Wrapper>
  );
};

export default TermsAndConditions;
