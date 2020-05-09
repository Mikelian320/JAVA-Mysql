<template>
  <main class="home">
    <img alt="Vue logo" :src="logoSrc">
    <HelloWorld msg="Welcome to table.greatwebtech.cn"/>
  </main>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue';
import logoSrc from './assets/logo.png';

export default {
  name: 'home',
  data() {
    return {
      logoSrc,
    };
  },
  components: {
    HelloWorld,
  },
};
</script>
