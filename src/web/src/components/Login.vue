<template>
  <div class="login">
    <el-card>
      <h2>Login</h2>
      <el-form
        class="login-form"
        :model="model"
        :rules="rules"
        ref="form"
        @submit.native.prevent="login"
      >
        <el-form-item prop="username">
          <el-input
            v-model="model.username"
            placeholder="Username"
            prefix-icon="el-icon-user-solid"
          >
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="model.password"
            placeholder="Password"
            type="password"
            prefix-icon="el-icon-lock"
          >
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            :loading="loading"
            class="login-button"
            type="primary"
            native-type="submit"
            block
            >Login</el-button
          >
        </el-form-item>
        <!-- <a class="forgot-password" href="https://oxfordinformatics.com/"
          >Forgot password ?</a
        > -->
      </el-form>
    </el-card>
  </div>
</template>

<script>
import checkLogin from '@/utils/checkLogin';

export default {
  name: 'login',
  data() {
    return {
      model: {
        username: '',
        password: '',
      },
      loading: false,
      rules: {
        username: [
          {
            required: true,
            message: 'Username is required',
            trigger: 'blur',
          },
          {
            min: 4,
            message: 'Username length should be at least 5 characters',
            trigger: 'blur',
          },
        ],
        password: [
          { required: true, message: 'Password is required', trigger: 'blur' },
          {
            min: 5,
            message: 'Password length should be at least 5 characters',
            trigger: 'blur',
          },
        ],
      },
    };
  },
  methods: {
    simulateLogin() {
      return new Promise((resolve) => {
        setTimeout(resolve, 800);
      });
    },
    async login() {
      const valid = await this.$refs.form.validate();
      if (!valid) {
        return;
      }
      this.loading = true;
      await this.simulateLogin();
      this.loading = false;
      const { username, password } = this.model;

      const isLogin = checkLogin(username, password);

      if (isLogin) {
        this.$message.success('登陆成功');
        localStorage.setItem('username', username);
        localStorage.setItem('password', password);
        this.$router.push('/');
      } else {
        this.$message.error('登陆失败');
      }
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.login {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-button {
  width: 100%;
  margin-top: 40px;
}
.login-form {
  width: 290px;
}
.forgot-password {
  margin-top: 10px;
}
</style>
<style>
.login .el-input__prefix {
  background: rgb(238, 237, 234);
  left: 0;
  height: calc(100% - 3px);
  left: 1px;
  top: 1px;
  border-radius: 3px;
}
.login .el-input__prefix .el-input__icon {
  width: 30px;
}
.login .el-input input {
  padding-left: 35px;
}
.login .el-card {
  padding-top: 0;
  padding-bottom: 30px;
}
h2 {
  letter-spacing: 1px;
  padding-bottom: 20px;
}
.login .el-card {
  width: 340px;
  display: flex;
  justify-content: center;
}
</style>
