# price_comparison


### User 

#### 1. 概述
- **描述**: `User`对象代表网站中的用户，包含用户的基本信息和行为。
- **用途**: 用于用户注册、登录、信息管理等功能。

#### 2. 属性定义
| 属性名       | 类型       | 描述                       | 是否必填 | 默认值      |
|--------------|------------|----------------------------|----------|-------------|
| id           | int        | 用户唯一标识符              | 是       | 自动生成    |
| username     | String     | 用户名                     | 是       | -           |
| password     | String     | 密码                       | 是       | -           |
| email        | String     | 用户邮箱                   | 是       | -           |
| phone        | String     | 用户手机号                 | 是       | ""           |
| createdAt    | DateTime   | 用户创建时间               | 是       | 当前时间    |
| updatedAt    | DateTime   | 用户信息更新时间           | 否       | 当前时间    |
| isActive     | Boolean    | 用户账户是否激活           | 否       | true        |
| role         | String     | 用户角色（如 admin, user） | 否       | USER        |

#### 3. 方法定义
| 方法名               | 返回类型    | 描述                     |
|----------------------|-------------|--------------------------|
| register()           | User        | 注册新用户               |
| login()              | Boolean     | 用户登录                 |
| updateProfile()      | User        | 更新用户信息             |
| deactivateAccount()   | Boolean     | 注销用户账户             |
| getUserDetails()     | User        | 获取用户详细信息         |

#### 4. API 接口
- **注册用户**
  - **URL**: `/api/users/register`
  - **方法**: `POST`
  - **请求体**:
    ```json
    {
      "username": "string",
      "password": "string",
      "email": "string"
    }
    ```
  - **响应**:
    ```json
    {
      "userId": "string",
      "username": "string",
      "email": "string",
      "createdAt": "DateTime"
    }
    ```

- **用户登录**
  - **URL**: `/api/users/login`
  - **方法**: `POST`
  - **请求体**:
    ```json
    {
      "username": "string",
      "password": "string"
    }
    ```
  - **响应**:
    ```json
    {
      "token": "string",
      "userId": "string",
      "username": "string"
    }
    ```

#### 5. 数据库设计
- **用户表结构**
```sql
CREATE TABLE users (
  userId VARCHAR(255) PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,
  updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  isActive BOOLEAN DEFAULT true,
  role VARCHAR(50) DEFAULT 'user'
);
