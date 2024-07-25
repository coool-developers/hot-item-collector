import { createRouter, createWebHistory } from 'vue-router';
import MainPage from '@/components/MainPage.vue';
import ProfileUpdate from '@/components/ProfileUpdate.vue';
import Profile from "@/components/Profile.vue";

const routes = [
  {
    path: '/',
    name: 'MainPage',
    component: MainPage,
  },
  {
    path: '/profile-update',
    name: 'ProfileUpdate',
    component: ProfileUpdate,
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
