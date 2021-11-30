import styled from 'styled-components';
import { space, layout, border } from 'styled-system';
import { FormProps } from '../../interfaces/styles';

export const Container = styled.div<FormProps>`
  display: flex;
  flex-direction: column;
  align-items: center;

  margin: auto 1em;
  padding: 1em;
  width: 100%;
  max-height: 700px;

  border-radius: 50px;
  border: 2px solid ${({ theme }) => theme.colors.secondary};

  ${space}
  ${layout}
`;

export const Base = styled.form`
  display: flex;
  flex-wrap: wrap;
  max-width: 300px;
  padding: 10px;
`;

export const Input = styled.input<FormProps>`
  width: 100%;
  min-height: 45px;
  border: 1px solid ${({ theme }) => theme.colors.secondary};
  border-radius: 30px;
  text-indent: 10px;
  ${space}
  ${border}

  &:focus {
    outline: none;
    box-shadow: 0 0 6px 0 ${({ theme }) => theme.colors.secondary};
  }
`;

export const PasswordContainer = styled.div`
  position: relative;
  width: 100%;
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

export const Submit = styled.button`
  margin-top: 1em;
  padding: 0px 20px;
  width: 100%;
  min-width: 250px;
  min-height: 45px;
  font-size: 17px;
  font-weight: 700;
  color: ${({ theme }) => theme.colors.white};
  background: #006fd1;
  border: none;
  border-radius: 30px;
`;

export const InputFieldWrapper = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
`;
