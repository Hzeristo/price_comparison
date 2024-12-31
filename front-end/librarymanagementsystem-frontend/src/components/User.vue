<template>
    <el-scrollbar height="100%" style="width: 100%;">
        <div style="margin-top: 20px; margin-left: 40px; font-size: 2em; font-weight: bold; ">用户管理</div>

        <!-- 遮罩 -->
        <div v-if="!isLoggedIn" class="overlay">
            <div class="overlay-content">
                <p>请先登录</p>
                <el-button type="primary" @click="redirectToLogin">登录</el-button>
            </div>
        </div>
        
        <div v-else>
            <div class="cardBox">
                <div style="font-size: 25px; font-weight: bold; margin-bottom: 15px;">基本信息</div>
                <el-button style="margin-bottom: 10px;" @click="logout()">退出登录</el-button>
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 15px;">
                    <span style="font-size: 20px">用户名</span>
                    <div style="display: flex; align-items: center;">
                        <span v-if="!editFields.username" style="font-size: 20px; color:lightslategray">{{ user.username }}</span>
                        <el-input v-else v-model="user.username" style="width: 60%;"></el-input>
                        <el-button 
                            plain 
                            size="mini" 
                            style="margin-left: 10px;" 
                            @click="toggleEdit('username')">
                            {{ editFields.username ? '保存' : '编辑' }}
                        </el-button>
                    </div>
                </div>
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 15px;">
                    <span style="font-size: 20px">密码</span>
                    <div style="display: flex; align-items: center;">
                        <span v-if="!editFields.password" style="font-size: 20px; color:lightslategray">
                            <span v-for="i in user.password.length/5" :key="i">*</span>
                        </span>
                        <div v-else style="display: flex; flex-direction: column; gap: 5px;">
                            <!-- 输入原密码 -->
                            <el-input
                                v-model="originalPassword"
                                type="password"
                                placeholder="输入原密码"
                                style="width: 100%;"
                            ></el-input>
                            <!-- 输入新密码 -->
                            <el-input
                                v-model="newPassword"
                                type="password"
                                placeholder="输入新密码"
                                style="width: 100%;"
                            ></el-input>
                        </div>
                        <el-button 
                            plain 
                            size="mini" 
                            style="margin-left: 10px;" 
                            @click="toggleEditPassword('password')">
                            {{ editFields.password ? '保存' : '编辑' }}
                        </el-button>
                    </div>
                </div>
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 15px;">
                    <span style="font-size: 20px">邮箱</span>
                    <div style="display: flex; align-items: center;">
                        <span v-if="!editFields.email" style="font-size: 20px; color:lightslategray">{{ user.email }}</span>
                        <el-input v-else v-model="user.email" style="width: 60%;"></el-input>
                        <el-button 
                            plain 
                            size="mini" 
                            style="margin-left: 10px;" 
                            @click="toggleEdit('email')">
                            {{ editFields.email ? '保存' : '编辑' }}
                        </el-button>
                    </div>
                </div>
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 15px;">
                    <span style="font-size: 20px">手机号</span>
                    <div style="display: flex; align-items: center;">
                        <span v-if="!editFields.phone" style="font-size: 20px; color:lightslategray">{{ user.phone }}</span>
                        <el-input v-else v-model="user.phone" style="width: 60%;"></el-input>
                        <el-button 
                            plain 
                            size="mini" 
                            style="margin-left: 10px;" 
                            @click="toggleEdit('phone')">
                            {{ editFields.phone ? '保存' : '编辑' }}
                        </el-button>
                    </div>
                </div>
                <div v-if="user.role=='ADMIN'" style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 15px;">
                    <span style="font-size: 20px">权限组</span>
                    <span style="font-size: 20px; color:lightslategray">{{user.role}}</span>
                </div>
            </div>

            <el-dialog
                title="登录京东"
                v-model="jdlogindialog"
                width="30%"
                >
                <!-- 登录表单 -->
                <el-form :model="jdloginForm" ref="jdloginForm" label-width="80px">
                    <el-form-item label="用户名" prop="username" :rules="[{ required: true, message: '请输入用户名', trigger: 'blur' }]">
                    <el-input v-model="jdloginForm.username" placeholder="请输入用户名"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
                    <el-input v-model="jdloginForm.password" placeholder="请输入密码" show-password></el-input>
                    </el-form-item>
                    <el-form-item>
                    <el-button type="primary" @click="handleJDLogin()">登录京东</el-button>
                    <el-button @click="jdlogindialog = false">取消</el-button>
                    </el-form-item>
                </el-form>
            
            </el-dialog>

            <el-dialog
                title="登录淘宝"
                v-model="taobaologindialog"
                width="30%"
                >
                <!-- 登录表单 -->
                <el-form :model="taobaologinForm" ref="taobaologinForm" label-width="80px">
                    <el-form-item label="用户名" prop="username" :rules="[{ required: true, message: '请输入用户名', trigger: 'blur' }]">
                    <el-input v-model="taobaologinForm.username" placeholder="请输入用户名"></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
                    <el-input v-model="taobaologinForm.password" placeholder="请输入密码" show-password></el-input>
                    </el-form-item>
                    <el-form-item>
                    <el-button type="primary" @click="handleTaobaoLogin()">登录淘宝</el-button>
                    <el-button @click="taobaologindialog = false">取消</el-button>
                    </el-form-item>
                </el-form>
            
            </el-dialog>

            <div class="cardBox">
                <el-scrollbar height="100%" style="width: 100%; height: 100%; ">
                    <div style="font-size: 25px; font-weight: bold; margin-bottom: 15px;">降价提醒商品</div>
                        <div v-for="item in fetchedFavorites">
                            <div class="itemBox">
                                <div class="itemBoxLeft">
                                    <div style="font-size: 20px; font-weight: bold; color: #025a9a"><a :href="item.url">{{ item.name }}</a></div>
                                    <div style="font-size: 15px;">当前价格（元） ：{{ item.price }}</div>
                                    <div v-if="item.isDetailsVisible">
                                        <div v-if="!item.isEditingFrequency" style="font-size: 15px;">
                                            查询频率：{{ formatFrequency(item.reminderFrequency) }}
                                            <el-button size="" @click="enableEditing(item)">编辑</el-button>
                                        </div>

                                        <!-- 编辑模式 -->
                                        <div v-else style="font-size: 15px;">
                                            查询频率：
                                            <el-select v-model="item.reminderFrequency" placeholder="选择查询频率" style="width: 200px;">
                                                <el-option
                                                    v-for="option in frequencyOptions"
                                                    :key="option.value"
                                                    :label="option.label"
                                                    :value="option.value"
                                                ></el-option>
                                            </el-select>
                                            <el-button size="" type="success" @click="saveFrequency(item)">保存</el-button>
                                            <el-button size="" @click="cancelEditing(item)">取消</el-button>
                                        </div>
                                    </div>
                                    <div v-if="item.isDetailsVisible" style="font-size: 15px;">提醒方式：邮件</div>
                                </div>
                                <div class="itemBoxRight">
                                    <el-button-group>
                                        <el-button plain @click="item.isDetailsVisible = !item.isDetailsVisible">{{ item.isDetailsVisible ? '隐藏详情' : '显示详情' }}</el-button>
                                        <el-button plain @click="confirmDelete(item)">删除</el-button>
                                    </el-button-group>
                                </div>

                                <el-dialog
                                    v-model="deleteDialogVisible"
                                    width="30%"
                                    :before-close="handleDialogClose"
                                >
                                    <span>确定要删除该收藏吗？</span>
                                    <template #footer>
                                        <el-button @click="deleteDialogVisible = false">取消</el-button>
                                        <el-button type="danger" @click="deleteItem">确认删除</el-button>
                                    </template>
                                </el-dialog>
                            </div>
                        </div>
                </el-scrollbar>
            </div>
        </div>

        

    </el-scrollbar>
</template>

<script>
import axios from 'axios'
import { favoriteServiceApi, spiderServiceApi, userServiceApi } from '@/api';
import { onMounted } from 'vue';

export default {
    data() {
        return {
            jdlogindialog: false,
            taobaologindialog: false,
            jdloginForm: {
                username: "",
                password: "",
            },
            rules: {
                username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
                password: [{ required: true, message: "请输入密码", trigger: "blur" }],
            },
            taobaologinForm: {
                username: "",
                password: "",
            },
            rules: {
                username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
                password: [{ required: true, message: "请输入密码", trigger: "blur" }],
            },
            frequencyOptions: [
                { value: 86400, label: '每天' },
                { value: 604800, label: '每周' },
                { value: 2592000, label: '每月' },
            ],
            isLoggedIn: false,
            modifyFavoriteVisible: false,
            editFields: {
                username: false,
                password: false,
                email: false,
                phone: false,
                role: false
            },
            originalPassword: '',
            newPassword: '',
            user: {
                id: 0,
                username: '',
                password: '',
                phone: '',
                email: '',
                role: '',
            },
            update: {
                reminderFrequency: 0,
            },
            fetchedFavorites:[
                {
                    id: '1',
                    name: '',
                    price: '',
                    reminderFrequency: 0,
                    url: '',
                    isDetailsVisible: false,
                    isEditingFrequency: false,
                    originalFrequency: 0,
                }
            ],
            deleteDialogVisible: false, // 控制对话框显示
            itemToDelete: null, // 当前要删除的项目
        }
    },
    methods: {
        formatFrequency(seconds) {
            if (seconds < 60) {
                return `${seconds} 秒`;
            } else if (seconds < 60 * 60) {
                return `${Math.round(seconds / 60)} 分钟`;
            } else if (seconds < 24 * 60 * 60) {
                return `${Math.round(seconds / (60 * 60))} 小时`;
            } else {
                return `${Math.round(seconds / (24 * 60 * 60))} 天`;
            }
        },
        getFriendlyBoundType(type) {
            return this.boundTypeMap[type] || '未知提醒类型';
        },
        enableEditing(item) {
            item.isEditingFrequency = true;
            item.originalFrequency = item.reminderFrequency; // 记录原始值
        },
        cancelEditing(item) {
            this.update.reminderFrequency = '';
            item.reminderFrequency = item.originalFrequency;
            item.isEditingFrequency = false; // 退出编辑模式
        },
        async saveFrequency(item) {
            try {
                // 更新提醒频率值
                this.update.reminderFrequency = item.reminderFrequency;

                // 调用更新接口
                await this.updateFavorite(item);

            } catch (error) {
                console.error('更新提醒频率时发生错误:', error);

                // 恢复原始提醒频率
                item.reminderFrequency = item.originalFrequency;

                // 显示错误信息
                this.$message.error(error.message || '提醒频率更新失败，请稍后重试');
            } finally {
                // 退出编辑模式
                item.isEditingFrequency = false;
            }
        },
        async fetchUserFavorite(userId) {
            await favoriteServiceApi
                .get('/favorite/user', {
                    params: { userId }, // 查询参数
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('jwt')}`, // 认证头
                    },
                })
                .then((response) => {
                    if (response.data && response.data.success) {
                        const {success, data, message} = response.data;
                        const items = data.map(entry => ({
                            ...entry.item, // 展开 item 对象的所有字段
                            reminderFrequency: entry.reminderFrequency // 添加 reminderFrequency
                        }));
                        // 保存提取后的结果
                        this.fetchedFavorites = items;
                        this.$message.success('用户收藏加载成功');
                    } else {
                        this.$message.error(response.data.message || '加载用户收藏失败');
                    }
                })
                .catch((error) => {
                    console.error('加载用户收藏时发生错误:', error);
                    this.$message.error('加载用户收藏失败，请稍后重试');
                });
        },
        async handleJDLogin() {
            this.$refs.jdloginForm.validate(async (valid) => {
                if (valid) {
                    try {
                        const response = await spiderServiceApi.post("/spider/jd/login", null, {
                            params: {
                                username: this.jdloginForm.username,
                                password: this.jdloginForm.password,
                            },
                            headers: {
                                Authorization: `Bearer ${localStorage.getItem('jwt')}`, // 认证头
                            }
                        });
                        if (response.data && response.data.success) {
                            this.$message.success('登录成功');
                            this.jdlogindialog = false;
                        } else {
                            this.$message.error(response.data.message || '登录失败');
                        }
                    } catch (error) {
                        console.error('登录时发生错误:', error);
                        this.$message.error('登录失败，请稍后重试');
                    }
                } else {
                    console.log("表单验证失败");
                }
            });
        },
        async handleTaobaoLogin() {
            this.$refs.taobaologinForm.validate(async (valid) => {
                if (valid) {
                    try {
                        const response = await spiderServiceApi.post("/spider/taobao/login", null, {
                            params: {
                                username: this.taobaologinForm.username,
                                password: this.taobaologinForm.password,
                            },
                            headers: {
                                Authorization: `Bearer ${localStorage.getItem('jwt')}`, // 认证头
                            }
                        });
                        if (response.data && response.data.success) {
                            this.$message.success('登录成功');
                            this.taobaologindialog = false;
                        } else {
                            this.$message.error(response.data.message || '登录失败');
                        }
                    } catch (error) {
                        console.error('登录时发生错误:', error);
                        this.$message.error('登录失败，请稍后重试');
                    }
                } else {
                    console.log("表单验证失败");
                }
            });
        },


        async updateFavorite(item) {
            try {
                const response = await favoriteServiceApi.put(
                    '/favorite/update', // API 路径
                    this.update, // 更新字段作为请求体发送
                    {
                        params: {
                            userId: this.user.id, // 查询参数
                            itemId: item.id, // 查询参数
                        },
                        headers: {
                            Authorization: `Bearer ${localStorage.getItem('jwt')}`, // 认证头
                        },
                    }
                );

                if (response.data && response.data.success) {
                    this.$message.success('收藏更新成功');
                    this.checkLogin();
                } else {
                    this.$message.error(response.data.message || '收藏更新失败');
                }
            } catch (error) {
                console.error('更新收藏时发生错误:', error);
                this.$message.error('更新收藏失败，请稍后重试');
            }
        },
        confirmDelete(item) {
            this.itemToDelete = item; // 设置当前要删除的项目
            this.deleteDialogVisible = true; // 打开对话框
        },
        async deleteItem() {
            try {
                const response = await favoriteServiceApi.delete('/favorite/delete', {
                    params: {
                        userId: this.user.id, 
                        itemId: this.itemToDelete.id 
                    }, // 替换为后端所需的参数
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('jwt')}`, // 身份认证
                    },
                });
                
            } catch (error) {
                console.error('删除时发生错误:', error);
                this.$message.error('删除失败，请稍后重试');
            } finally {
                this.deleteDialogVisible = false; // 确保对话框关闭
            }
        },
        handleDialogClose() {
            this.itemToDelete = null; // 清除选择的项目
        },
        logout() {
            localStorage.removeItem('jwt');
            this.checkLogin();
        },
        async checkLogin() {
            const token = localStorage.getItem('jwt');
            if (token) {
                try {
                    this.isLoggedIn = true;
                    await this.fetchUserData();
                    await this.fetchUserFavorite(this.user.id);
                } catch (error) {
                    console.error('Error during login check:', error);
                    this.isLoggedIn = false;
                }
            } else {
                this.isLoggedIn = false;
            }
        },
        async fetchUserData() {
            const token = localStorage.getItem('jwt');
            const username = localStorage.getItem('username');
            await userServiceApi.get('/user/username', {
                params: { username }, // 使用 `params` 传递查询参数
                headers: { Authorization: `Bearer ${token}` },
            })
            .then((response) => {
                const { success, data, message } = response.data;
                this.user = data;
                console.log('user id:', this.user.id);
            })
            .catch((error) => {
                console.error('Failed to fetch user data:', error);
                this.isLoggedIn = false;

            });
        },
        redirectToLogin() {
            window.location.href = '/login'; // 跳转到登录页面
        },
        toggleEdit(field) {
            if (this.editFields[field]) {
                this.updateUserData(field);
            }
            this.editFields[field] = !this.editFields[field];
        },
        toggleEditPassword(field) {
            const token = localStorage.getItem('jwt');
            const username = localStorage.getItem('username');

            userServiceApi.get('/user/validate', {
                params: {
                    username: username,
                    password: this.newPassword
                }
            })
            .then((response) => {
                const { success, data, message } = response.data;
                if (this.editFields[field] && success) {
                    this.updateUserData(field);
                }
                this.editFields[field] = !this.editFields[field];
            }).catch((error) => {
                console.error('Failed to validate password:', error);
            });
            
        },
        updateUserData(field) {
            const token = localStorage.getItem('jwt');
            const username = localStorage.getItem('username');
            
            
            // 创建更新的 payload，只包含被编辑的字段
            const payload = { [field]: this.user[field] };

            // 发送 PUT 请求
            userServiceApi.put('/user/update', payload, {
                params: { username },  // 使用 `params` 传递 username
                headers: { Authorization: `Bearer ${token}` },
            })
            .then((response) => {
                const { success, message, data } = response.data;
                if (success) {
                    this.user = data;  // 更新用户信息
                    if (field === 'username') {
                        localStorage.setItem('username', this.user.username);
                    }
                    this.$message.success('信息已更新');
                } else {
                    this.$message.error(message || '更新失败');
                }
            })
            .catch((error) => {
                console.error('Failed to update user data:', error);
                this.$message.error('更新失败');
            });
        },
    },
    mounted() {
        this.checkLogin();
    }
}
</script>

<style scoped>
.cardBox {
    min-height: 200px;
    height: auto;
    width: 90%; /* 宽度改为父容器的 80% */
    margin: 40px auto; /* 垂直外边距为 40px，水平居中 */
    padding: 15px 20px 7.5px; /* 简化 padding 写法 */
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}

.itemBox{
    width: 100%;
    min-height: 50px;
    height: auto;
    margin-top: 10px;
    padding: 10px;
    display: flex;
    position: relative;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.itemBoxLeft{
    width: 80%;
    float: left;
}
.itemBoxRight{
    position: absolute; /* 子元素设置为绝对定位 */
    top: 20px; 
    right: 0; 
    width: 30%;
    display: flex;
    justify-content: center; 
    align-items: center; 
    float: left;
}

.overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.7);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 9999;
}

.overlay-content {
    text-align: center;
    font-size: 1.5em;
    color: white;
    padding: 20px;
    border-radius: 10px;
}

.login-container {
  width: 400px;
  margin: 100px auto;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  background-color: #fff;
}
</style>