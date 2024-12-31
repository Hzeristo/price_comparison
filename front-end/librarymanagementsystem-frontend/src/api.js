import axios from 'axios';

// 基础 API 配置
const API_URLS = {
  userService: "http://localhost:8081",
  itemService: "http://localhost:8082",
  favoriteService: "http://localhost:8083",
  spiderService: "http://localhost:8091"
};

// 创建 axios 实例
const userServiceApi = axios.create({
  baseURL: API_URLS.userService,
});

const itemServiceApi = axios.create({
  baseURL: API_URLS.itemService,
});

const favoriteServiceApi = axios.create({
  baseURL: API_URLS.favoriteService,
});

const spiderServiceApi = axios.create({
  baseURL: API_URLS.spiderService,
});

// 导出实例
export { userServiceApi, itemServiceApi, favoriteServiceApi, spiderServiceApi };
