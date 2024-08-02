import { createApp } from 'vue';
import App from '@/App';
import router from '@/router';
import axios from "axios";

axios.defaults.baseURL = 'http://43.201.84.28:8080';

const app = createApp(App);
app.config.globalProperties.axios = axios;
app.use(router);

app.mount('#app');