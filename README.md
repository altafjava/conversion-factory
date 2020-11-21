# Conversion Factory

Conversion Factory is web application build on Spring as Backend & ReactJS as Frontend. Both projects are combined within a single Spring Boot based project.

## Screenshot

![image](https://user-images.githubusercontent.com/43504471/99863290-7ebf1080-2bc3-11eb-98b1-96c0b856f530.png)

## Steps to convert file

- First Upload any file from your local computer/mobile
- Choose the file type of the uploaded file
- Choose the file type of the target file
- Click on the Convert Button

## New Features!

- Currently you can convert CSV to JSON & vice versa
- Handling all possible errors

## Tech

Conversion Factory Backend part is simpley created with the help of Spring Starter Project and Frontend part has created with **_create-react-app_** command.

Below are the list of used maven dependency for Backend:

- [Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok) - Automatic Resource Management, automatic generation of getters, setters, equals, hashCode and toString, and more
- [Jackson Dataformat CSV](https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-csv) - Support for reading and writing CSV-encoded data via Jackson abstractions

Below are the list of used node dependency for Frontend:

- [Axios](https://www.npmjs.com/package/axios) - Promise based HTTP client for the browser and node.js

## Development

Want to contribute? Great!

Conversion Factoru uses Spring Boot Starter & CREATE-REACT-APP for fast developing.
Make a change in your file and instantaneously see your updates!

> To work in Backend part:

1. Clone the project from [https://github.com/altafjava/conversion-factory.git](https://github.com/altafjava/conversion-factory.git)
2. Open your favorite IDE(Eclipse, IntelliJ IDEA, VS Code) and import the project.
3. Here you can work in Backend part

> To work in Frontend part:

1. I shall recommend VSCode.
2. Open VSCode and goto _File->Open Folder_.
3. Goto the directory where you have cloned the project then choose the **_ui_** folder and open it.
4. Alternately you can simple open the terminal/command prompt upto the **_/conversion-factory/ui/_** directory and type code .(dot). It will open the frontend project.
5. `npm install`
6. `npm start`
7. `npm run build`
8. As we know `npm run build` will create the minified version of the app which can be served as static website. And we also know that Spring Boot application can easily serve static pages if we put inside `src/main/resources/static` directory. Hence after building the React app we are copying the build files into `src/main/resources/static/` directory. For this use the below command.
9. `npm run copy`

> Now again go the the Backend project start the application. After starting open the browser and simply type this url [http://localhost:8080/](http://localhost:8080/). It will appear like the above screenshot. Now you know what to do.

## Building for source

For production release:

```sh
$
```

Generating pre-built zip archives for distribution:

```sh
$
```

## Todos

- Add multiple file types for conversion

## License

None

**Free Software, Hell Yeah!**
