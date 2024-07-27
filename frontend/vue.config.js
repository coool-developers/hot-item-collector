const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8081, // Vue 개발 서버 포트
    proxy: {
      '/prepare/order': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: { '^/prepare/order': '/prepare/order' }, // 경로를 그대로 유지
      },
      '/prepare/payment': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: { '^/prepare/payment': '/prepare/payment' }, // 경로를 그대로 유지
      },
      '/payments/verify': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: { '^/payments/verify': '/payments/verify' }, // 경로를 그대로 유지
      },
    },
  },
});