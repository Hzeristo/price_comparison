<template>
    <div class="login-container">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="登录" name="login">
          <el-form :model="loginForm" ref="loginForm" :rules="rules" label-width="80px">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-button
              type="primary"
              @click="login"
              style="width: 100%; margin-top: 20px;"
            >
              登录
            </el-button>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册" name="register">
          <el-form :model="registerForm" ref="registerForm" :rules="rules" label-width="80px">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" v-model="registerForm.password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="registerForm.phone" placeholder="请输入手机号"></el-input>
            </el-form-item>
            <el-button
              type="primary"
              @click="register"
              style="width: 100%; margin-top: 20px;"
            >
              注册
            </el-button>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  import { userServiceApi } from "@/api";
  
  export default {
    data() {
      return {
        activeTab: "login",
        loginForm: {
          username: "",
          password: "",
        },
        registerForm: {
          username: "",
          password: "",
          email: "",
          phone: "",
        },
        rules: {
          username: [
            { required: true, message: "请输入用户名", trigger: "blur" },
            { min: 5, message: "用户名长度至少为5位", trigger: "blur" },
            {
                pattern: /^[a-zA-Z0-9_]{5,255}$/,
                message: "用户名必须仅包含字母、数字和下划线",
                trigger: "blur",
            }
          ],
          password: [
            { required: true, message: "请输入密码", trigger: "blur" },
            { min: 6, message: "密码长度至少为6位", trigger: "blur" },
            {
              pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,255}$/,
              message: "密码必须为8-255个字符，包含至少一个大写字母、一个小写字母和一个数字",
              trigger: "blur",
            },
          ],
          email: [
            { required: true, message: "请输入邮箱", trigger: "blur" },
            { type: "email", message: "邮箱格式不正确", trigger: "blur" },
          ],
          phone: [
            {
              pattern: /^1[2-9]\d{9}$/,
              message: "手机号格式不正确",
              trigger: "blur",
            },
          ],
        },
      };
    },
    methods: {
      // 登录逻辑
      async login() {
        this.$refs.loginForm.validate(async (valid) => {
          if (valid) {
            const { username, password } = this.loginForm;
            try {
                const response = await userServiceApi.get('/user/validate', {
                    params: {
                        username: username,
                        password: password
                    }
                });
                const { success, data, message } = response.data;
                if (success) {
                  const token = data; // 提取 token
                  console.log('Token:', token);

                  // 存储 token 到 localStorage
                  localStorage.setItem('jwt', token);
                  localStorage.setItem('username', username);

                  // 跳转到主页面
                  this.$message.success("登录成功！");
                  this.$router.push("/user");
                } else {
                this.$message.error(message || "登录失败");
              }
            } catch (error) {
              this.$message.error(error.response?.data?.message || "请求失败，请稍后再试！");
            }
          } else {
            this.$message.error("请正确填写登录表单");
          }
        });
      },
  
      // 注册逻辑
      async register() {
        this.$refs.registerForm.validate(async (valid) => {
          if (valid) {
            try {
              const response = await userServiceApi.post("/user/new",
                this.registerForm
              );
  
              // 注册成功后自动登录
              this.loginForm.username = this.registerForm.username;
              this.loginForm.password = this.registerForm.password;
              this.$message.success("注册成功！请登录");
              this.activeTab = "login";
            } catch (error) {
              this.$message.error(
                error.response?.data?.message || "注册失败"
              );
            }
          } else {
            this.$message.error("请正确填写注册信息");
          }
        });
      },
    },
  };
  </script>
  
  <style scoped>
  .login-container {
    width: 400px;
    margin: 100px auto;
    padding: 40px;
    box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    background-color: #ffffff;
  }
  </style>
  