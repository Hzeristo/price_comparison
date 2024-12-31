<template>
  <div>
    <div class="main" style="overflow-y: hidden;">
      <el-container>
        <!-- 顶部导航栏 -->
        <el-header class="header" style="display: flex; align-items: center; justify-content: space-between;">
          <div class="logo">
            <img src="./components/icons/logo.png"
              style="margin-right: 20px; height: 40px; vertical-align: middle;" />
            <span style="font-size: large; font-family: 'Microsoft YaHei'; color: black; font-weight: bold;">
              商品比价平台
            </span>
          </div>
          <!-- 顶部菜单 -->
          <el-menu
            mode="horizontal"
            active-text-color="#ffd04b"
            background-color="#0270c1"
            text-color="#fff"
            default-active="1"
            :router="true"
            class="menu"
          >
            <el-menu-item index="user">
              <el-icon><Tickets /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="items">
              <el-icon><Tickets /></el-icon>
              <span>商品页面</span>
            </el-menu-item>
          </el-menu>
        </el-header>

        <!-- 主体内容 -->
        <el-container style="width: 100%;">
          <el-main style="height: 100%; width: 100%;">
            <el-scrollbar height="100%">
              <RouterView
                class="content"
                style="height: 90vh; max-height: 100%; background-color: white; color: black;"
              />
            </el-scrollbar>
          </el-main>
        </el-container>
      </el-container>
    </div>
  </div>
</template>

<script>
import { RouterView } from 'vue-router'

export default {}
</script>

<style scoped>
#app {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: #dcdcdc;
  width: 100vw;
  height: 100vh;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  width: 100%;
  min-height: 100%;
  height: auto;
  background-color: #dcdcdc;
}

.header {
  background-color: #ffffff;
  height: 60px;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.menu {
  flex-grow: 1;
  display: flex;
  justify-content: flex-end;
}
</style>
