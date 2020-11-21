import React, { useState } from 'react';
import ConversionService from '../api/ConversionService';
import { DANGER, INFO, SUCCESS, WARNING } from '../constant/Color';
import AlertMessage from './AlertMessage';
import './Conversion.css';

const uploadClick = () => {
  var inputTypeFile = document.getElementById('inputTypeFile');
  inputTypeFile.click();
};

const Conversion = () => {
  const [file, setFile] = useState('');
  const [isButtonDisabled, setButtonDisabled] = useState(false);
  const [input, setInput] = useState('csv');
  const [downloadUrl, setDownloadUrl] = useState('');
  const [output, setOutput] = useState('json');
  const [notification, setNotification] = useState({});

  const nameStartEndLength = 12;
  const fileTypes = [
    { key: 'csv', value: 'CSV' },
    { key: 'json', value: 'JSON' },
  ];

  const handleFileChange = (e) => {
    const btn = document.getElementById('convert-button');
    btn.innerHTML = "Convert <i id='spinner' class='fa spin'></i>";
    const uploadedFile = e.target.files[0];
    setFile(uploadedFile);
    if (uploadedFile === undefined) {
      setButtonDisabled(true);
    } else {
      const regex = /(?:\.([^.]+))?$/;
      let fileExtension = regex.exec(uploadedFile.name)[1];
      if (fileExtension === undefined) {
        showAlert('File has no extension. Please choose different file', WARNING);
        setButtonDisabled(true);
      } else {
        const isFileTypePresent = fileTypes.filter((fileType) => fileType.key === fileExtension).length > 0;
        if (!isFileTypePresent) {
          showAlert('Sorry, Currently we are not converting .' + fileExtension + ' file', INFO);
          setButtonDisabled(true);
        } else {
          setButtonDisabled(false);
          const sourceIndex = fileTypes.findIndex((fileType) => fileType.key === fileExtension);
          const select1 = document.getElementById('select1');
          select1.selectedIndex = sourceIndex;
          setInput(select1.value);
          const select2 = document.getElementById('select2');
          select2.selectedIndex = sourceIndex === 0 ? 1 : 0;
          setOutput(select2.value);
        }
      }
    }
  };
  const handleChangeSelect1 = (e) => {
    setInput(e.target.value);
  };
  const handleChangeSelect2 = (e) => {
    setOutput(e.target.value);
  };

  const handleConvert = (e) => {
    let btn = document.getElementById('convert-button');
    if (btn.innerText.trim() === 'Convert') {
      e.preventDefault();
      if (file.name === undefined) {
        showAlert('Please choose a file', WARNING);
      } else {
        let spinner = document.getElementById('spinner');
        disableButton(btn, spinner);
        ConversionService.convert(file, output)
          .then((response) => {
            showAlert('File successfully converted. Click the Download button to download', SUCCESS);
            setDownloadUrl(response.data.downloadUrl);
            btn.innerHTML = "Download <i id='spinner' class='fa spin'></i>";
            enableButton(btn, spinner);
          })
          .catch((error) => {
            showAlert(error.message, DANGER);
            enableButton(btn, spinner);
          });
      }
    } else if (btn.innerText === 'Download') {
    } else {
      e.preventDefault();
      showAlert("Don't try to be oversmart", INFO);
    }
  };
  const disableButton = (btn, spinner) => {
    spinner.classList.add('fa-spinner');
    btn.classList.add('button__disabled');
    btn.classList.remove('button__hover');
  };
  const enableButton = (btn, spinner) => {
    spinner.classList.remove('fa-spinner');
    btn.classList.remove('button__disabled');
    btn.classList.add('button__hover');
  };
  const showAlert = (message, background) => {
    setNotification({ hasError: true, message: message, background: background });
    setTimeout(() => setNotification({ hasError: false, message: message }), 3000);
  };

  return (
    <div className='main__container'>
      {notification.hasError && <AlertMessage background={notification.background} message={notification.message} />}
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
        <a
          id='convert-button'
          className={`button button__hover ${isButtonDisabled ? ' button__disabled' : ''}`}
          href={downloadUrl}
          onClick={handleConvert}
        >
          Convert <i id='spinner' className='fa spin'></i>
        </a>
      </div>
    </div>
  );
};

export default Conversion;
