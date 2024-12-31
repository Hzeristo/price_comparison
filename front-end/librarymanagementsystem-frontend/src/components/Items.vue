<template>
    <el-scrollbar height="100%" style="width: 100%; height: 100%; ">
        <!-- 标题和搜索框 -->
        <div style="margin-top: 20px; margin-left: 40px; font-size: 2em; font-weight: bold; ">商品查询
        </div> 

        <!-- 搜索区域 -->
        <div class="search-container">
        <!-- 搜索框 -->
            <div class="search-box" style="margin-bottom: 10px;">
                <el-input 
                v-model="toSearch" 
                placeholder="输入搜索关键词" 
                :prefix-icon="Search" 
                clearable 
                style="width: 40vw; min-width: 300px;"
                />
            </div>

        <!-- 按钮区域 -->
            <div style="display: flex; flex-direction: vertical; align-items: center;">
                <el-button 
                    type="primary" 
                    @click="jdlogindialog = true" 
                    :icon="Search" 
                    style="margin-bottom: 10px; width: 150px; height: 40px;"
                >
                    京东平台搜索
                </el-button>
                <el-button 
                    type="primary" 
                    @click="taobaologindialog = true" 
                    :icon="Search" 
                    style="margin-bottom: 10px; width: 150px; height: 40px;"
                >
                    淘宝平台搜索
                </el-button>
            </div>
        </div>


        <el-dialog
            title="京东用户身份验证"
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
                <el-button type="primary" @click="crawlJDItems()">搜索商品</el-button>
                <el-button @click="jdlogindialog = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog
            title="京东用户身份验证"
            v-model="jdlogindialog2"
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
                <el-button type="primary" @click="confirmJDLogin()">搜索商品</el-button>
                <el-button @click="jdlogindialog2 = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog
            title="淘宝用户身份验证"
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
                <el-button type="primary" @click="confirmJDLogin()">搜索商品</el-button>
                <el-button @click="taobaologindialog = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog
            title="淘宝用户身份验证"
            v-model="taobaologindialog2"
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
                <el-button type="primary" @click="confirmTaobaoLogin()">搜索商品</el-button>
                <el-button @click="taobaologindialog2 = false">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <!-- 商品显示区 -->
        <div style="display: flex; flex-wrap: wrap; justify-content: start;">

            <!-- 商品介绍卡 -->
            <div class="itemBox" v-for="item in fetchedItems">
                <div>
                    <!-- 卡片标题 -->
                    
                    <div class="imageBox" style="margin-top: 10px;">
                        <a :href="item.url"><img :src="item.image" style="width: 100%; height: auto;" /></a>
                    </div>

                    <el-divider />

                    <!-- 卡片内容 -->
                    <div style="margin-left: 10px; text-align: start; font-size: 16px;">
                        <p style="padding: 2.5px;"><span style="font-weight: bold;"><a :href="item.url">{{item.name}}</a></span></p>
                        <p style="padding: 2.5px;">平台价格：{{item.platform}}：{{item.price}}</p>
                    </div>

                    <el-divider />

                    <!-- 卡片操作 -->
                    <div style="margin-top: 10px;">
                        <el-button type="primary" :icon="Histogram" 
                            @click="getHistory(item.platform, item.skuid, item.url)" 
                            circle />
                        <el-button type="primary" :icon="Switch" 
                            @click="loadComparisonItem(item)" 
                            circle />
                        <el-button type="primary" :icon="Plus" 
                            @click="newItemDialogVisible = true, this.newItemInfo.id = item.id, this.newItemInfo.name = item.name, this.newItemInfo.skuid = item.skuid, this.newItemInfo.price = item.price, this.newItemInfo.url = item.url, this.newItemInfo.image = item.image, this.newItemInfo.category = item.category, this.newItemInfo.platform = item.platform" 
                            circle />
                        <el-button type="danger" :icon="Minus" 
                            @click="removeItemDialogVisible = true"
                            circle />
                    </div>

                    
                </div>
            </div>
        </div>

        <el-dialog
            v-model="compareDialogVisible"
            title="比价"
            width="50%" align-center
        >
            <div>
                <el-table :data="comparisonItem" style="width: 100%">
                <el-table-column prop="platform" label="平台" align="center"></el-table-column>
                <el-table-column prop="price" label="价格" align="center"></el-table-column>
                <el-table-column prop="url" label="链接" align="center">
                    <template #default="scope">
                    <el-link :href="scope.row.url" target="_blank">查看商品</el-link>
                    </template>
                </el-table-column>
                </el-table>
            </div>
                
            <template #footer>
                <el-button @click="compareItem(comparisontemp)">查询其他平台价格</el-button>
                <el-button @click="compareDialogVisible = false">关闭</el-button>
            </template>
        </el-dialog>

        <!-- 对话框 -->
        <el-dialog
            v-model="historyDialogVisible"
            title="商品历史价格"
            width="50%" align-center
        >
            <div v-if="screenshotUrl">
                <img :src="screenshotUrl" alt="Screenshot" style="max-width: 100%;" />
            </div>
            <div v-else>
                <p>loading...</p>
            </div>
            <template #footer>
                <el-button @click="historyDialogVisible = false">关闭</el-button>
            </template>
        </el-dialog>

        <el-dialog v-model="newItemDialogVisible" title="关注商品" width="30%" align-center>
            <span>{{ this.newItemInfo.name }}</span>
            <template #footer>
                <span>
                    <el-button @click="newItemDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="createFavorite">确定</el-button>
                </span>
            </template>
        </el-dialog>

        <el-dialog v-model="removeItemDialogVisible" title="是否不再显示商品" width="30%" align-center>
            <template #footer>
                <span>
                    <el-button @click="removeItemDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="removeItem">确定</el-button>
                </span>
            </template>
        </el-dialog>

    </el-scrollbar>
</template>

<script>
import { favoriteServiceApi, spiderServiceApi, userServiceApi, itemServiceApi } from '@/api';
import { Delete, Edit, Search, Plus, Minus, Switch, Histogram } from '@element-plus/icons-vue'
import { formEmits } from 'element-plus';

export default {
    data() {
        return {
            testUrl: 'https://detail.tmall.com/item.htm?id=792533980252&ns=1&pisk=gmWjepwtXq0b2o4GZZqyRAgW2Iv1fuyElctOxGHqXKpvW_sh5ErgiK5W5aQWkxr0iCL1oKdGgF825dsGRuzULJScmdAT8yyFNGlu8pxxMx3w2gK6CnebU7rAmdvT50kTTJscJGqVAjK92utwbh3AWIhJeh8JDVKOWUdJvHuvBNpTVQKejAH96jh-ehY-kmd9DULJ0nL9XndTVgLkXdLOWIHtvxtzcUjbcjnlcFNTpfxIBABWcQFFDUnMeT-XEesAhAHTjnOXJiL8-AgocQIM1OmiCCSAtNxdkqU6STsRH6QLzj8AOhsF1wEZi3R1nLTOHJmByts1FIfrkjKBhEOdHIiUxiAONT9VHPch4gTvOL5zZrRwhZ1H-IFurNsWuNBXwqay76SPHQ_LzY_M11B2etU8dgljLeGihfiWtAtW8uZSsfvh7xjKaiwW2IKk4Pr7VXSMM3xW8uZSsfAvq3SaVuGFj&priceTId=214783a117355720085795085ed25a&skuId=5420605276721&spm=a21n57.1.item.1.39b0523cVTH0dm&utparam=%7B%22aplus_abtest%22%3A%22bfb7017d6e1f5ca5553a041ee057d6f1%22%7D&xxc=ad_ztc',
            testSku: '10126511277034',
            testName: 'ThinkPad联想ThinkBook16+ 2024AI全能轻薄本 16英寸英特尔酷睿Ultra 学生商务笔记本电脑 推荐Ultra9 32G 1T 0VCD165Hz',
            screenshotUrl: '',
            user: {
                id: 0,
                username: '',
                password: '',
                phone: '',
                email: '',
                role: '',
            },
            jdlogindialog: false,
            taobaologindialog: false,
            jdlogindialog2: false,
            taobaologindialog2: false,
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
            Delete, 
            Edit,
            Search,
            Plus,
            Switch,
            Histogram,
            Minus,
            historyDialogVisible: false,
            compareDialogVisible: false,
            newItemDialogVisible: false,
            removeItemDialogVisible: false,
            removeItemId: null,
            toSearchLocally: '',
            toSearch: '',
            item: {
                id: 0,
                name: '',
                skuid: '',
                price: '',
                url: '',
                image: '',
                category: '',
                platform: '',
            },
            newItemInfo: {
                id: 0,
                name: '',
                skuid: '',
                price: '',
                url: '',
                image: '',
                category: '',
                platform: '',
            },
            fetchedItems:[
                {
                    id: 0,
                    name: '',
                    skuid: '',
                    price: '',
                    url: '',
                    image: '',
                    category: '',
                    platform: '',
                }
            ],
            comparisonItem: [
                {
                    id: 0,
                    name: '',
                    skuid: '',
                    price: '',
                    url: '',
                    image: '',
                    category: '',
                    platform: '',
                }
            ],
            comparisontemp:{
                id: 0,
                name: '',
                skuid: '',
                price: '',
                url: '',
                image: '',
                category: '',
                platform: '',
            }
        }
    },
    methods: {
        // 搜索
        async searchItems() {
        },
        showRemoveDialog(id) {
            this.removeItemId = id; // 记录要删除的商品id
            this.removeItemDialogVisible = true;
        },
        async crawlJDItems() {
            try {
                const response = await spiderServiceApi.get('/spider/jd/search', {
                    params: { 
                        searchKeyWord: this.toSearch,
                        username: this.jdloginForm.username,
                        password: this.jdloginForm.password,
                    },
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('jwt')}`, // 认证头
                    }
                });

                const apiResponse = response.data;
                if (apiResponse.success) {
                    // 成功返回数据
                    this.fetchedItems = apiResponse.data; // 假设你有一个 `products` 数据变量存储结果
                    this.jdlogindialog = false;
                } else {
                    // 返回失败信息
                    this.$message.error(apiResponse.message || '搜索失败');
                }
                
            } catch (error) {
                // 捕获异常
                console.error('Error fetching products:', error);
                this.$message.error('网络请求失败，请稍后重试');
            }
        },
        async fetchJDItemById(skuid) {
            try {
                const response = await spiderServiceApi.get('/spider/jd/sku', {
                    params: { 
                        skuId: skuid,
                        store: "n",
                        username: this.jdloginForm.username,
                        password: this.jdloginForm.password,
                    },
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('jwt')}`, // 认证头
                    }
                })
                if (response.data && response.data.success) {
                    this.comparisonItem = response.data.data;
                } else {
                    this.$message.error(response.data.message || '获取商品信息失败');
                }
            } catch (error) {
                console.error('Error fetching product: ', error);
                this.$message.error('网络请求失败，请稍后重试');
            }
        },
        async fetchJDItemByName(name) {
            try {
                const response = await spiderServiceApi.get('/spider/jd/name', {
                    params: { 
                        name: name,
                        username: this.jdloginForm.username,
                        password: this.jdloginForm.password,
                    },
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('jwt')}`, // 认证头
                    }
                })
                if (response.data && response.data.success) {
                    this.comparisonItem.push(response.data.data);
                } else {
                    this.$message.error(response.data.message || '获取商品信息失败');
                }
            } catch (error) {
                console.error('Error fetching product: ', error);
                this.$message.error('网络请求失败，请稍后重试');
            }
        },
        async fetchTaobaoItemByName(name) {
            try {
                const response = await spiderServiceApi.get('/spider/taobao/name', {
                    params: { 
                        name: name,
                        username: this.taobaologinForm.username,
                        password: this.taobaologinForm.password,
                    },
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('jwt')}`, // 认证头
                    }
                })
                if (response.data && response.data.success) {
                    this.comparisonItem.push(response.data.data);
                } else {
                    this.$message.error(response.data.message || '获取商品信息失败');
                }
            } catch (error) {
                console.error('Error fetching product: ', error);
                this.$message.error('网络请求失败，请稍后重试');
            }
        },
        loadComparisonItem(item) {
            this.compareDialogVisible = true;
            this.comparisonItem = [];
            this.comparisonItem.push(item);
            this.comparisontemp = item;
        },
        async compareItem(item) {
            if(item.platform === '淘宝') {
                await this.handleJDLogin();
                await this.fetchJDItemByName(item.name);
            } else if(item.platform === '京东') {
                await this.handleTaobaoLogin();
                await this.fetchTaobaoItemByName(item.name);
            }
        },
        handleTaobaoLogin() {
            this.taobaologindialog2 = true; // 打开淘宝登录对话框
            return new Promise((resolve, reject) => {
                this.confirmTaobaoLogin = async () => {
                    this.$refs.taobaologinForm.validate((valid) => {
                        if (valid) {
                            console.log("淘宝登录成功");
                            this.taobaologindialog2 = false; // 关闭对话框
                            resolve(); // 登录成功继续主流程
                        } else {
                            this.$message.error("请完成表单填写！");
                            reject(new Error("表单验证失败"));
                        }
                    });
                };
            });
        },
        handleJDLogin() {
            this.jdlogindialog2 = true; // 打开京东登录对话框
            return new Promise((resolve, reject) => {
                this.confirmJDLogin = async () => {
                    this.$refs.jdloginForm.validate((valid) => {
                        if (valid) {
                            console.log("京东登录成功");
                            this.jdlogindialog2 = false; // 关闭对话框
                            resolve(); // 登录成功继续主流程
                        } else {
                            this.$message.error("请完成表单填写！");
                            reject(new Error("表单验证失败"));
                        }
                    });
                };
            });
        },
        async crawlTaobaoItems() {
            try {
                const response = await spiderServiceApi.get('/spider/taobao/search', {
                    params: { 
                        searchKeyWord: this.toSearch,
                        username: this.taobaologinForm.username,
                        password: this.taobaologinForm.password,
                    },
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('jwt')}`, // 认证头
                    }
                });

                const apiResponse = response.data;
                if (apiResponse.success) {
                    // 成功返回数据
                    this.fetchedItems = apiResponse.data; // 假设你有一个 `products` 数据变量存储结果
                } else {
                    // 返回失败信息
                    this.$message.error(apiResponse.message || '搜索失败');
                }
                
            } catch (error) {
                // 捕获异常
                console.error('Error fetching products:', error);
                this.$message.error('网络请求失败，请稍后重试');
            }
        },
        // 确认删除操作
        removeItem() {
            this.fetchedItems = this.fetchedItems.filter(item => item.id !== this.removeItemId);
            this.removeItemDialogVisible = false; // 关闭对话框
        },
        async createFavorite() {
            this.newItemDialogVisible = false;
            try {
                const token = localStorage.getItem('jwt');
                const username = (localStorage.getItem('username'));
                await this.fetchUserData(username);
                await this.fetchFavoriteItem(this.newItemInfo.skuid);
                console.log('id:', this.newItemInfo.id)
                const favorite = {
                    user: this.user, // 提供用户 ID
                    item: {
                        id: this.newItemInfo.id,
                        name: this.newItemInfo.name,
                        skuid: this.newItemInfo.skuid,
                        price: this.newItemInfo.price,
                        url: this.newItemInfo.url,
                        image: this.newItemInfo.image,
                        category: this.newItemInfo.category,
                        platform: this.newItemInfo.platform,
                    },
                    reminderFrequency: 0, // 默认值，具体可由用户设置
                };
                const response = await favoriteServiceApi.post('/favorite/new', favorite,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    }
                );
                if (response.data && response.data.success) {
                    this.$message.success('收藏创建成功');
                } else {
                    this.$message.error(response.data.message || '收藏创建失败');
                }
            } catch (error) {
                console.error('创建收藏时发生错误:', error);
                this.$message.error('创建收藏失败，请稍后重试');
            }
        },
        async fetchFavoriteItem() {
            try {
                const token = localStorage.getItem('jwt');
                const response = await itemServiceApi.get('/items/sku', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                    params: { skuId: this.newItemInfo.skuid }
                });
                if (response && response.status === 200 && response.data) {
                // Handle the successful response
                    console.log("Fetched item successfully:", response.data);
                    this.newItemInfo = response.data; // Assuming you have a property to store the fetched item
                } else {
                    console.warn("Unexpected response format or status code", response);
                }
            } catch (error) {
                // Handle errors gracefully
                if (error.response) {
                    // Server responded with a status other than 2xx
                    console.error("Error fetching item, status:", error.response.status, error.response.data);
                } else if (error.request) {
                    // Request was made but no response received
                    console.error("No response received:", error.request);
                } else {
                    // Something happened during setup
                    console.error("Error setting up the request:", error.message);
                }
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
            })
            .catch((error) => {
                console.error('Failed to fetch user data:', error);
            });
        },
        test() {
            spiderServiceApi.get('/spider/items', {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('jwt')}`,
                },
            })
            .then((response) => {
                console.log(response.data);
            })
            .catch((error) => {
                console.error('Error fetching products:', error);
            });
        },
        async getHistory(platform, skuid, url) {
            this.screenshotUrl = ''
            this.historyDialogVisible = true
            if(platform === '京东') {
                await this.fetchJDScreenshot(skuid);
            } else if(platform === '淘宝') {
                await this.fetchTaobaoScreenshot(url);
            }
        },
        async fetchJDScreenshot(skuid) {
            await spiderServiceApi
                .get("/spider/jd/priceHistory", {
                    params: {
                        skuId: skuid,
                    },
                    responseType: "blob", // 告诉 Axios 以 Blob 格式处理响应
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('jwt')}`,
                    },
                })
                .then((response) => {
                    // 创建一个临时 URL，用于展示图片
                    this.screenshotUrl = URL.createObjectURL(response.data);
                })
                .catch((error) => {
                    console.error("Error fetching screenshot:", error);
                    this.error = "Failed to load screenshot.";
                });
        },
        async fetchTaobaoScreenshot(url) {
            await spiderServiceApi
                .get("/spider/taobao/priceHistory", {
                    params: {
                        url: url,
                    },
                    responseType: "blob", // 告诉 Axios 以 Blob 格式处理响应
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('jwt')}`,
                    },
                })
                .then((response) => {
                    // 创建一个临时 URL，用于展示图片
                    this.screenshotUrl = URL.createObjectURL(response.data);
                })
                .catch((error) => {
                    console.error("Error fetching screenshot:", error);
                    this.error = "Failed to load screenshot.";
                });
        },
    }
}
</script>

<style scoped>
.itemBox {
    height: flex;
    width: 300px;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    text-align: center;
    margin-top: 40px;
    margin-left: 27.5px;
    margin-right: 10px;
    padding: 7.5px;
    padding-right: 10px;
    padding-top: 15px;
}

.imageBox {
    width: 280px; /* 设置图片 box 的宽度 */
    height: 280px; /* 设置图片 box 的高度 */
    overflow: hidden; /* 超出部分隐藏 */
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #f5f5f5; /* 可选的背景颜色 */
    border: 1px solid #ddd; /* 可选的边框 */
}

.imageBox img {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain; /* 确保图片按比例缩放并居中 */
}

.search-container {
  display: flex;                /* 使用 Flex 布局 */
  flex-direction: column;       /* 子元素垂直排列 */
  align-items: center;          /* 子元素水平居中 */
  justify-content: center;      /* 子元素垂直居中 */
  width: 100%;                  /* 宽度占满父容器 */
  margin-top: 20px;             /* 顶部外边距，用于与其他内容分隔 */
}

.search-box {
  width: 100%;                  /* 搜索框宽度占满 */
  text-align: center;           /* 搜索框内文字居中 */
}

.el-input {
  width: 100%;
}

</style>

