const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8081, // vue 포트 번호 설정
    proxy: {
      '/products': {
        target: 'http://43.201.84.28:8080',
        changeOrigin: true,
      },
      '/prepare/order': {
        target: 'http://43.201.84.28:8080',
        changeOrigin: true,
      },
      '/prepare/payments': {
        target: 'http://43.201.84.28:8080',
        changeOrigin: true,
      },
    },
  },
  baseUrl: 'http:/43.201.84.28:8080'
});