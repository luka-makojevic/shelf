import { emailRegex, passwordRegex } from './regex';

describe('Regex', () => {
  it('should not let "test" to pass as email', async () => {
    expect(emailRegex.test('test')).toEqual(false);
  });

  it('should not let "test@email.c" to pass as email', async () => {
    expect(emailRegex.test('test@email.c')).toEqual(false);
  });

  it('should let "test@email.com" to pass as email', async () => {
    expect(emailRegex.test('test@email.com')).toEqual(true);
  });

  it('should let "ännö@email.com" to pass as email', async () => {
    expect(emailRegex.test('ännö@email.com')).toEqual(true);
  });

  it('should not let "test" to pass as password', async () => {
    expect(passwordRegex.test('test')).toEqual(false);
  });

  it('should not let "testing123" to pass as password', async () => {
    expect(passwordRegex.test('testing123')).toEqual(false);
  });

  it('should not let "Testing123" to pass as password', async () => {
    expect(passwordRegex.test('Testing123')).toEqual(false);
  });

  it('should not let "Testing@" to pass as password', async () => {
    expect(passwordRegex.test('Testing@')).toEqual(false);
  });

  it('should let "Testing@123" to pass as password', async () => {
    expect(passwordRegex.test('Testing@123')).toEqual(true);
  });

  it('should not let "Ilovetöäst123" to pass as password', async () => {
    expect(passwordRegex.test('Ilovetöäst123')).toEqual(false);
  });

  it('should  let "Ilovetöäst@123" to pass as password', async () => {
    expect(passwordRegex.test('Ilovetöäst@123')).toEqual(true);
  });
});
