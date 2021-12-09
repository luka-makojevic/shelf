export const LocalStorage = {
  get(key: string) {
    return localStorage.getItem(key);
  },
  set(key: string, value: string | {}) {
    localStorage.setItem(key, JSON.stringify(value));
  },
  remove(key: string) {
    localStorage.removeItem(key);
  },
  clear() {
    localStorage.clear();
  },
};
