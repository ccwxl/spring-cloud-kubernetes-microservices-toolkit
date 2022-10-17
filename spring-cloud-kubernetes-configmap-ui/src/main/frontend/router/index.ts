import {
    createRouter,
    createWebHistory,
    NavigationGuardNext,
    RouteLocationNormalized
} from 'vue-router'
import routes from './routes'

// NProgress
// @ts-ignore
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

const router = createRouter({
    history: createWebHistory(
        import.meta.env.MODE === 'production' ? '/dolphinscheduler/ui/' : '/'
    ),
    routes
})

interface metaData {
    title?: string
    activeMenu?: string
    showSide?: boolean
    auth?: Array<string>
}

/**
 * Routing to intercept
 */
router.beforeEach(
    async (
        to: RouteLocationNormalized,
        from: RouteLocationNormalized,
        next: NavigationGuardNext
    ) => {
        NProgress.start()
        const metaData: metaData = to.meta
        if (
            metaData.auth?.includes('ADMIN_USER') &&
            metaData.activeMenu === 'security'
        ) {
            to.fullPath = '/security/token-manage'
            next({name: 'token-manage'})
        } else {
            next()
        }

        NProgress.done()
    }
)

router.afterEach(() => {
    NProgress.done()
})

export default router