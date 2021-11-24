import { Wrapper } from '../../components/layout/layout.styles';

import TextCard from '../../components/textCard';
import TosText from '../../fixtures/data';

const TermsAndConditions = () => {
  const title = 'Terms of Service';
  const subtitle = 'Last eddited on';
  const text = TosText;
  return (
    <Wrapper justifyContent="center">
      <TextCard title={title} text={text} subtitle={subtitle} />
    </Wrapper>
  );
};

export default TermsAndConditions;
