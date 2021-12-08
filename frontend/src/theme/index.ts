export const theme = {
  breakpoints: ['480px', '768px', '996px', '1200px'],
  space: [
    '0px',
    '4px',
    '8px',
    '16px',
    '32px',
    '64px',
    '128px',
    '256px',
    '512px',
  ],
  size: [
    '100px',
    '200px',
    '300px',
    '400px',
    '500px',
    '600px',
    '700px',
    '800px',
    '900px',
  ],

  fonts: {
    body: 'Source Sans Pro, sans-serif',
    heading: 'inherit',
  },

  fontSizes: [
    '12px',
    '14px',
    '16px',
    '18px',
    '20px',
    '24px',
    '32px',
    '48px',
    '64px',
  ],
  fontWeights: {
    body: 400,
    heading: 700,
    bold: 700,
  },
  lineHeights: {
    body: 1.5,
    heading: 1.125,
  },

  colors: {
    white: '#fff',
    black: '#000',
    primary: '#006FD1',
    secondary: '#BFBFBF',
    danger: 'red',
    shadow: '#3C4D6B',
    success: 'green',
    info: '#1E90FF',
  },

  styles: {
    root: {
      fontFamily: 'body',
      lineHeight: 'body',
      fontWeight: 'body',
    },
    h1: {
      variant: 'text.heading',
      fontSize: 5,
    },
    h2: {
      variant: 'text.heading',
      fontSize: 4,
    },
    h3: {
      variant: 'text.heading',
      fontSize: 3,
    },
    h4: {
      variant: 'text.heading',
      fontSize: 2,
    },
    h5: {
      variant: 'text.heading',
      fontSize: 1,
    },
    h6: {
      variant: 'text.heading',
      fontSize: 0,
    },
    pre: {
      fontFamily: 'monospace',
      overflowX: 'auto',
      code: {
        color: 'inherit',
      },
    },
    code: {
      fontFamily: 'monospace',
      fontSize: 'inherit',
    },
    table: {
      width: '100%',
      borderCollapse: 'separate',
      borderSpacing: 0,
    },
    th: {
      textAlign: 'left',
      borderBottomStyle: 'solid',
    },
    td: {
      textAlign: 'left',
      borderBottomStyle: 'solid',
    },
  },
};
