import Home from '../views/admin/Home';

export default [
    {
        path: '/dashboard',
        component: Home,
        children: [
            {
                path: '',
                name: 'Dashboard',
                component: () => import('../views/admin/Dashboard.vue')
            },

            {
                path: 'user',
                name: 'User',
                component: () => import('../views/admin/User.vue')
            },
            {
                path: 'add_user',
                name: 'AddUser',
                component: () => import('../views/admin/AddUser.vue')
            }
        ],
    }
]