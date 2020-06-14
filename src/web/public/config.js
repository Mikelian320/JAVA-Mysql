const isLocalhost = window.location.href.includes('localhost');

window.CONFIG = {
  SEARCH_ORIGIN: isLocalhost ? 'http://table-test.greatwebtech.cn/search/' : '/search/',
  // SEARCH_ORIGIN: backend url,
  LOAD_DATA_ENTER_PAGE: true,
  // LOAD_DATA_ENTER_PAGE: false,
};
