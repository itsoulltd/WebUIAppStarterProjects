import vue from 'vue';
import VueRouter from 'vue-router';

vue.use(VueRouter);

import RootRouter from './RootRouter';
import AuthRouter from './AuthRouter';

const routes = [
    ...RootRouter,
    ...AuthRouter
];

const router = new VueRouter({
    mode: 'history',
    routes:routes
});

export default router