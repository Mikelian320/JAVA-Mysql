import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('./views/Table.vue'),
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('./views/Login.vue'),
  },
];

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});
