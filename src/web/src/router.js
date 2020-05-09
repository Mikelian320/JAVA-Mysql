import Vue from 'vue';
import Router from 'vue-router';
import { TYPE } from '@/constants/config';
import Home from './views/Home.vue';

Vue.use(Router);

let routes;

if (TYPE === 'only-search') {
  routes = [
    {
      path: '/',
      name: 'home',
      component: () => import('./views/Table.vue'),
    },
  ];
} else {
  routes = [
    {
      path: '/',
      name: 'home',
      component: Home,
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/About.vue'),
    },
    {
      path: '/table',
      name: 'table',
      component: () => import('./views/Table.vue'),
    },
  ];
}

routes.push({
  path: '/login',
  name: 'login',
  component: () => import('./views/Login.vue'),
});

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
});
