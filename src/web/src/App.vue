<template>
  <div id="app">
    <nav id="nav">
      <div v-if="username">Hello, <strong class="username">{{username}}</strong>!</div>
      <router-link to="/">Home</router-link> |
      <router-link to="/login">Login</router-link>
    </nav>
    <router-view/>
  </div>
</template>

<script>

export default {
  data() {
    return {
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
  color: #409EFF;
}
#nav .username {
  color: #409EFF;
}
</style>
