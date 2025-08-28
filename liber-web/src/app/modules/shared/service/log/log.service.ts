import { Injectable } from '@angular/core';
import { LogLevel, LOG_LEVELS } from './log-level';

@Injectable({
  providedIn: 'root',
})
export class LogService {
  currentLog: number;

  constructor() {
    this.currentLog = LOG_LEVELS.DEBUG;
  }

  setLogLevel(level: LogLevel) {
    this.currentLog = level as number;
  }

  fine(message: any) {
    if (this.currentLog >= LOG_LEVELS.FINE) {
      console.log(`%c${message}`, 'color:magenta');
    }
  }

  debug(message: any) {
    if (this.currentLog >= LOG_LEVELS.DEBUG) {
      console.log(`%c${message}`, 'color:gray');
    }
  }

  info(message: any) {
    if (this.currentLog >= LOG_LEVELS.INFO) {
      console.log(`%c${message}`, 'color:teal');
    }
  }

  warn(message: any) {
    if (this.currentLog >= LOG_LEVELS.WARN) {
      console.log(`%c${message}`, 'color:orange');
    }
  }

  error(message: any) {
    if (this.currentLog >= LOG_LEVELS.ERROR) {
      console.log(`%c${message}`, 'color:red');
    }
  }
}
