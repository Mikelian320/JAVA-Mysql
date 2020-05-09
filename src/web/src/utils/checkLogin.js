import { USER_LIST } from '@/constants/config';

export default (username, password) => USER_LIST
  .some(user => user.username === username && user.password === password);
