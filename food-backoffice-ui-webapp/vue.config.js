module.exports = {
  devServer: {
    proxy: {
      '/food': {
        target: 'http://localhost:8882', 
        changeOrigin: true,
      }
    }
  }
}