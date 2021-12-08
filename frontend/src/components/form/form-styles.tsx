import styled from 'styled-components';
import { theme } from '../../theme';
import { FormProps } from '../../utils/interfaces/styles';

export const FormContainer = styled.div<FormProps>`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: auto ${theme.space[3]};
  padding: ${theme.space[3]};
  border-radius: 50px;
  border: 2px solid ${theme.colors.secondary};
  max-width: 380px;
  width: 100%;
`;

export const Base = styled.form`
  display: flex;
  flex-wrap: wrap;
  max-width: 300px;
  padding: ${theme.space[2]};
`;

export const Spinner = styled.img`
  filter: invert(1);
  width: 30px;
  height: 30px;

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
`;

export const InputFieldWrapper = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  min-height: 75px;
`;
