import React, { Fragment } from 'react';
import './App.css';
import Conversion from './components/Conversion';
import Header from './components/Header';

function App() {
  return (
    <Fragment>
      <Header />
      <Conversion />
    </Fragment>
  );
}

export default App;
