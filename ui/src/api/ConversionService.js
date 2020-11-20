import Axios from 'axios';

class ConversionService {
  convert(file, outputFormat) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('outputFormat', outputFormat);
    return Axios({
      method: 'POST',
      url: 'http://localhost:8080/convert',
      data: formData,
      headers: { 'Access-Control-Allow-Origin': '*', 'Content-Type': 'multipart/form-data' },
    });
  }
}
export default new ConversionService();
