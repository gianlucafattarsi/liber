export const LOG_LEVELS = {
  FATAL: 0,
  ERROR: 1,
  WARN: 2,
  INFO: 3,
  DEBUG: 4,
  FINE: 5,
};

export class LogLevel {
  static readonly FINE = LOG_LEVELS.FINE;
  static readonly DEBUG = LOG_LEVELS.DEBUG;
  static readonly INFO = LOG_LEVELS.INFO;
  static readonly WARN = LOG_LEVELS.WARN;
  static readonly ERROR = LOG_LEVELS.ERROR;
  static readonly FATAL = LOG_LEVELS.FATAL;
}
