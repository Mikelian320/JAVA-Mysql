const isLocalhost = window.location.href.includes('localhost');

window.CONFIG = {
  SEARCH_ORIGIN: isLocalhost ? 'http://table-test.greatwebtech.cn/search/' : '/search/',
  // SEARCH_ORIGIN: backend url,
};
