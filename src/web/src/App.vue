<template>
  <div id="app">
    <nav id="nav" v-if="showNav">
      <div v-if="username">Hello, <strong class="username">{{username}}</strong>!</div>
      <router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link> |
      <router-link to="/table">Table</router-link> |
      <router-link to="/login">Login</router-link>
    </nav>
    <router-view/>
  </div>
</template>

<script>
import { TYPE } from '@/constants/config';

export default {
  data() {
    return {
      showNav: TYPE !== 'only-search',
      username: localStorage.getItem('username'),
    };
  },
};
</script>

<style>
body {
  margin: 0;
  padding: 0;
}
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
#nav .username {
  color: #42b983;
}
</style>
