import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { ThemeProvider } from 'styled-components';
import { BrowserRouter } from 'react-router-dom';
import { theme } from '../theme';
import FormValidation from '../pages/register/formValidation';

const mockRegister = jest.fn((email, password, confirmPassword, areTermsRead) =>
  Promise.resolve({ email, password, confirmPassword, areTermsRead })
);

describe('Login', () => {
  beforeEach(() => {
    render(
      <ThemeProvider theme={theme}>
        <BrowserRouter>
          <FormValidation registerTest={mockRegister} />
        </BrowserRouter>
      </ThemeProvider>
    );
  });

  it('should display required error when value is invalid', async () => {
    fireEvent.submit(screen.getByRole('button'));

    expect(await screen.findAllByRole('alert')).toHaveLength(6);
    expect(mockRegister).not.toBeCalled();
  });

  it('should display matching error when email is invalid', async () => {
    fireEvent.input(screen.getByPlaceholderText('Email'), {
      target: {
        value: 'test',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Password'), {
      target: {
        value: 'Nemanja@123',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Confirm Password'), {
      target: {
        value: 'Nemanja@123',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('First Name'), {
      target: {
        value: 'Nemanja',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Last Name'), {
      target: {
        value: 'Pavlovic',
      },
    });

    fireEvent.click(screen.getByRole('checkbox'));

    fireEvent.submit(screen.getByRole('button'));

    expect(await screen.findAllByRole('alert')).toHaveLength(1);
    expect(mockRegister).not.toBeCalled();
    expect(screen.getByPlaceholderText('Email').value).toBe('test');
    expect(screen.getByPlaceholderText('Password').value).toBe('Nemanja@123');
    expect(screen.getByPlaceholderText('Confirm Password').value).toBe(
      'Nemanja@123'
    );
    expect(screen.getByPlaceholderText('First Name').value).toBe('Nemanja');
    expect(screen.getByPlaceholderText('Last Name').value).toBe('Pavlovic');
    expect(screen.getByRole('checkbox').checked).toEqual(true);
  });

  it('should display matching error when password and confirmPassword do not match', async () => {
    fireEvent.input(screen.getByPlaceholderText('Email'), {
      target: {
        value: 'test@email.com',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Password'), {
      target: {
        value: 'Nemanja@123',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Confirm Password'), {
      target: {
        value: 'Nemanja',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('First Name'), {
      target: {
        value: 'Nemanja',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Last Name'), {
      target: {
        value: 'Pavlovic',
      },
    });

    fireEvent.click(screen.getByRole('checkbox'));

    fireEvent.submit(screen.getByRole('button'));

    expect(await screen.findAllByRole('alert')).toHaveLength(1);
    expect(mockRegister).not.toBeCalled();
    expect(screen.getByPlaceholderText('Email').value).toBe('test@email.com');
    expect(screen.getByPlaceholderText('Password').value).toBe('Nemanja@123');
    expect(screen.getByPlaceholderText('Confirm Password').value).toBe(
      'Nemanja'
    );
    expect(screen.getByPlaceholderText('First Name').value).toBe('Nemanja');
    expect(screen.getByPlaceholderText('Last Name').value).toBe('Pavlovic');
    expect(screen.getByRole('checkbox').checked).toEqual(true);
  });

  it('should display min length error when password is invalid', async () => {
    fireEvent.input(screen.getByPlaceholderText('Email'), {
      target: {
        value: 'test@email.com',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Password'), {
      target: {
        value: 'test',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Confirm Password'), {
      target: {
        value: 'test',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('First Name'), {
      target: {
        value: 'Nemanja',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Last Name'), {
      target: {
        value: 'Pavlovic',
      },
    });

    fireEvent.click(screen.getByRole('checkbox'));

    fireEvent.submit(screen.getByRole('button'));

    expect(await screen.findAllByRole('alert')).toHaveLength(1);
    expect(mockRegister).not.toBeCalled();
    expect(screen.getByPlaceholderText('Email').value).toBe('test@email.com');
    expect(screen.getByPlaceholderText('Password').value).toBe('test');
    expect(screen.getByPlaceholderText('Confirm Password').value).toBe('test');
    expect(screen.getByPlaceholderText('First Name').value).toBe('Nemanja');
    expect(screen.getByPlaceholderText('Last Name').value).toBe('Pavlovic');
    expect(screen.getByRole('checkbox').checked).toEqual(true);
  });

  it('should not display error when value is valid', async () => {
    fireEvent.input(screen.getByPlaceholderText('Email'), {
      target: {
        value: 'test@email.com',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Password'), {
      target: {
        value: 'Nemanja@123',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Confirm Password'), {
      target: {
        value: 'Nemanja@123',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('First Name'), {
      target: {
        value: 'Nemanja',
      },
    });

    fireEvent.input(screen.getByPlaceholderText('Last Name'), {
      target: {
        value: 'Pavlovic',
      },
    });

    fireEvent.click(screen.getByRole('checkbox'));

    fireEvent.submit(screen.getByRole('button'));

    await waitFor(() => expect(screen.queryAllByRole('alert')).toHaveLength(0));
    expect(mockRegister).toBeCalledWith(
      'test@email.com',
      'Nemanja@123',
      'Nemanja@123',
      true
    );
    expect(screen.getByPlaceholderText('Email').value).toBe('');
    expect(screen.getByPlaceholderText('Password').value).toBe('');
    expect(screen.getByPlaceholderText('Confirm Password').value).toBe('');
    expect(screen.getByPlaceholderText('First Name').value).toBe('');
    expect(screen.getByPlaceholderText('Last Name').value).toBe('');
    expect(screen.getByRole('checkbox').checked).toEqual(false);
  });
});
