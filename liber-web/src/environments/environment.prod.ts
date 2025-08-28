import { LogLevel } from '../app/modules/shared/service/log/log-level'

export const environment = {
    production: true,

    logLevel: LogLevel.ERROR,

    basePath: 'http://localhost:8989/liber',

    websocketUri: 'ws://localhost:8989/liber/ws',
}
