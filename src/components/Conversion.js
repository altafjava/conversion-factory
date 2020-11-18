import React, { useState } from 'react';
import ConversionService from '../api/ConversionService';
import { DANGER } from '../constant/Color';
import AlertMessage from './AlertMessage';
import './Container.css';

const uploadClick = () => {
  var inputTypeFile = document.getElementById('inputTypeFile');
  inputTypeFile.click();
};

const Conversion = () => {
  const [file, setFile] = useState('');
  const [input, setInput] = useState('csv');
  const [output, setOutput] = useState('json');
  const [notification, setNotification] = useState({});
  const nameStartEndLength = 12;

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
    let btn = document.querySelector('button');
    let spinner = document.getElementById('spinner');
    spinner.classList.add('fa-spinner');
    btn.disabled = true;
    btn.className = 'button__disabled';
    if (file.name === undefined) {
      showAlert('Please choose a file');
    } else {
      const regex = /(?:\.([^.]+))?$/;
      let fileExtension = regex.exec(file.name)[1];
      if (fileExtension === undefined) {
        showAlert('File has no extension. Please choose different file');
      } else {
        ConversionService.convert(file, output)
          .then((response) => {
            console.log(response.data);
            spinner.classList.remove('fa-spinner');
            btn.disabled = false;
            btn.classList.remove('button__disabled');
            btn.textContent = 'Download';
          })
          .catch((error) => {
            console.log(error);
            spinner.classList.remove('fa-spinner');
            btn.disabled = false;
            btn.classList.remove('button__disabled');
          });
      }
    }
  };
  const showAlert = (message) => {
    setNotification({ hasError: true, message: message });
    setTimeout(() => setNotification({ hasError: false, message: message }), 3000);
  };

  return (
    <div className='main__container'>
      {notification.hasError && <AlertMessage background={DANGER} message={notification.message} />}
      <div className='container'>
        <div className='hide'>
          <input id='inputTypeFile' type='file' onChange={handleFileChange} />
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
                  <td>
                    {file && file.name.length > nameStartEndLength + nameStartEndLength + 8
                      ? file.name.substring(0, nameStartEndLength) +
                        '....' +
                        file.name.substring(file.name.length - nameStartEndLength, file.name.length)
                      : file.name}
                  </td>
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
          <select defaultValue='csv' id='select1' onChange={handleChangeSelect1}>
            <option value='csv'>CSV</option>
            <option value='json'>JSON</option>
            <option value='parquet'>PARQUET</option>
            <option value='xml'>XML</option>
            <option value='avro'>AVRO</option>
          </select>
        </div>
        <div className='div__to'>
          <h1>to</h1>
        </div>
        <div className='div__select2'>
          <select defaultValue='json' id='select2' onChange={handleChangeSelect2}>
            <option value='csv'>CSV</option>
            <option value='json'>JSON</option>
            <option value='parquet'>PARQUET</option>
            <option value='xml'>XML</option>
            <option value='avro'>AVRO</option>
          </select>
        </div>
      </div>
      <div className='div__button'>
        <button id='button' onClick={handleConvert}>
          Convert <i id='spinner' className='fa spin'></i>
        </button>
      </div>
    </div>
  );
};

export default Conversion;
