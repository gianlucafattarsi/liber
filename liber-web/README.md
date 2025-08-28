# Liber WEB (Documentation being updated)

This is a project is a WEBApp that contains the client-side part of the project. By consuming REST APIs and using websockets it manages displaying and editing data in a user-friendly way. Frameworks, programming methodologies and languages used:

**<img height="25" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/angular.png"> [Angular](https://angular.dev)**

Angular is a development platform for building mobile and desktop web applications.

**<img height="25" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/node_js.png"> [Node.js](https://nodejs.org/en)**

**<img height="25" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/typescript.png"> [TypeScript](https://www.typescriptlang.org)**

**<img height="25" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/npm.png"> [npm](https://www.npmjs.com/)**

**<img height="25" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/websocket.png"> [websocket](https://en.wikipedia.org/wiki/WebSocket)**

**<img height="25" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/rest.png"> [REST](https://en.wikipedia.org/wiki/REST)**

**<img height="25" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/html.png"> HTML**

**<img height="25" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/sass.png"> Sass**

**<img height="25" src="https://raw.githubusercontent.com/marwin1991/profile-technology-icons/refs/heads/main/icons/tailwind_css.png"> [Tailwind CSS](https://tailwindcss.com)**

## Running the application locally

### Clone the repo

```shell
git clone https://github.com/gianlucafattarsi/liber
cd liber-web
```

### Install npm packages

Install the `npm` packages described in the `package.json` and verify that it works:

```shell
npm install
npm start
```

The `npm start` command builds (compiles TypeScript and copies assets) the application into `dist/`, watches for changes to the source files, and runs `lite-server` on port `4200`.

Shut it down manually with `Ctrl-C`.

#### npm scripts

These are the most useful commands defined in `package.json`:

* `npm start` - runs the TypeScript compiler, asset copier, and a server at the same time, all three in "watch mode".
* `npm run build` - runs the TypeScript compiler and asset copier once.
* `npm run build:watch` - runs the TypeScript compiler and asset copier in "watch mode"; when changes occur to source files, they will be recompiled or copied into `dist/`.
* `npm run openapigen` - runs the script to automatically generate all classes and services based on the openapi.json file provided by the RESTApi managed by the liber-api project