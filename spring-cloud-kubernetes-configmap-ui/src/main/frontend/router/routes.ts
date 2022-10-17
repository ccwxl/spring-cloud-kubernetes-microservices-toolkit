import {RouteRecordRaw} from 'vue-router'
import Login from '../views/login.vue'

const baseRoutes = [
    {
        // 重定向，用来指向一打开网页就跳转到哪个路由
        path: '/',
        redirect: '/main'
    },
    {
        // 首页
        path: '/main',
        name: 'Main',
        component: () => import('../views/main.vue'),
        children: [// 开始嵌套路由，这下面的所有路由都是Main路由的子路由
            {
                path: '/', // 嵌套路由里默认是哪个网页
                redirect: '/index'
            },
            {
                path: '/index', // 首页的路由
                name: 'Index',
                component: () => import('../views/index.vue')
            },
            {
                path: '/setting', // 设置页面的路由
                name: 'Setting',
                component: () => import('../views/setting.vue')
            }
        ]
    },
    {
        path: '/*', // 注意，这里不是嵌套理由了，这是为了设置404页面，一定要放在最后面，这样当服务器找不到页面的时候就会返回404
        name: '404',
        component: () => import('../views/404.vue')
    }
];

/**
 * Login page
 */
const loginPage: RouteRecordRaw[] = [
    {
        path: '/login',
        name: 'login',
        component: Login,
        meta: {
            auth: []
        }
    }
]

const routes: RouteRecordRaw[] = [...baseRoutes, ...loginPage]

export default routes