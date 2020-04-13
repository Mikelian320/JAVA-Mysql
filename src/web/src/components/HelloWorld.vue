<template>
  <div class="hello">
    <h1>Home</h1>
    <p>{{ msg }}</p>
    <h2>Links</h2>
    <ul>
      <!-- <li><a href="http://weather.greatwebtech.cn/weather" target="_blank" rel="noopener">weather</a></li> -->
      <li><a href="http://weather.greatwebtech.cn/sshInfo" target="_blank" rel="noopener">sshInfo</a></li>
      <li><a href="http://weather.greatwebtech.cn/list" target="_blank" rel="noopener">list</a></li>
      <li><a href="http://weather.greatwebtech.cn/timeline" target="_blank" rel="noopener">timeLine</a></li>
      <li><a href="http://weather.greatwebtech.cn/todoList" target="_blank" rel="noopener">todoList</a></li>
      <li><a href="http://code-craft.greatwebtech.cn" target="_blank" rel="noopener">code-craft</a></li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  props: {
    msg: String,
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h2 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
