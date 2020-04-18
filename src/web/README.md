# GreatWebTech
GreatWebTech is one of my website. Visit [Demo](http://table.greatwebtech.cn)

## Start
```
npm run serve
```

## Project setup
```
git clone https://github.com/double2kill/greatwebtech.git
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Run your tests
```
npm run test
```

### Run your unit tests
```
npm run test:unit
```

## Deploy to a new server

1. Install [nginx](https://www.runoob.com/linux/nginx-install-setup.html) to you server
2. `git clone [this repo]`
3. `npm install`
4. `npm run build`
5. you could see a latest `dist/` folder
6. copy `dist/*` to `nginx/html/*`

## Set env config after build

1. modify `dist/config.js` and then copy to your server html dir

```
window.CONFIG = {
  SEARCH_ORIGIN: 'http://table.greatwebtech.cn/search/',
  LOAD_DATA_ENTER_PAGE: true,
};
```

## CONFIG

SEARCH_ORIGIN 搜索的后端接口地址

LOAD_DATA_ENTER_PAGE, true/false 进入页面后是否加载数据
