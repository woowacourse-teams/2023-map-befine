type NotNull<T> = T extends null | undefined ? never : T;

function narrowNotNull<T>(value: T): NotNull<T> | null {
  return value !== null && value !== undefined ? (value as NotNull<T>) : null;
}
