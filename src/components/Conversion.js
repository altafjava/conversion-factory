import React, { useState } from 'react';
import './Container.css';

const uploadClick = () => {
  var inputTypeFile = document.getElementById('inputTypeFile');
  inputTypeFile.click();
};

const Conversion = () => {
  const [file, setFile] = useState('');
  const [input, setInput] = useState('coconut');
  const [output, setOutput] = useState('mango');

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };
  const handleChangeSelect1 = (e) => {
    setInput(e.target.value);
  };
  const handleChangeSelect2 = (e) => {
    setOutput(e.target.value);
  };
  const handleConvert = (e) => {
    e.preventDefault();
    alert('Converting ' + file.name + ' from ' + input + ' to ' + output);
  };
  return (
    <div className='main__container'>
      <div className='container'>
        <div className='hide'>
          <input id='inputTypeFile' type='file' accept='image/png, image/jpeg' onChange={handleFileChange} />
        </div>
        <div className='div__upload' onClick={uploadClick}>
          <img src='/assets/icons/upload-icon.svg' alt='upload files' width='50px' />
          <div>Choose File</div>
        </div>
        {file && (
          <div className='upload__details'>
            <table>
              <tbody>
                <tr>
                  <td className='align-right'>Name :</td>
                  <td>{file && file.name}</td>
                </tr>
                <tr>
                  <td className='align-right'>Size :</td>
                  {file && <td>{`${file.size / 1000} KB`}</td>}
                </tr>
                <tr>
                  <td className='align-right'>Type :</td>
                  <td>{file && file.type}</td>
                </tr>
              </tbody>
            </table>
          </div>
        )}
        <div className='div__select1'>
          <select defaultValue='coconut' id='select1' onChange={handleChangeSelect1}>
            <option value='grapefruit'>Grapefruit</option>
            <option value='lime'>Lime</option>
            <option value='coconut'>Coconut</option>
            <option value='mango'>Mango</option>
          </select>
        </div>
        <div className='div__to'>
          <h1>to</h1>
        </div>
        <div className='div__select2'>
          <select defaultValue='mango' id='select2' onChange={handleChangeSelect2}>
            <option value='grapefruit'>Grapefruit</option>
            <option value='lime'>Lime</option>
            <option value='coconut'>Coconut</option>
            <option value='mango'>Mango</option>
          </select>
        </div>
      </div>
      <div className='div__button'>
        <button onClick={handleConvert}>Convert</button>
      </div>
    </div>
  );
};

export default Conversion;
