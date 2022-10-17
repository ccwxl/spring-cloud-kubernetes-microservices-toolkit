import {RouteRecordRaw} from 'vue-router'
import Login from '../views/login/login.vue'

const baseRoutes = [
    {
        path: '/',
        redirect: '/configmap'
    },
    {
        path: '/configmap',
        name: 'configmap',
        component: () => import('../views/configmap/configmap.vue'),
        children: [
            {
                path: '',
                component: () => import('../views/configmap/content.vue')
            }
        ]
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