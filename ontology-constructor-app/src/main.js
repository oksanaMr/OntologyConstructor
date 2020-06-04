import Vue from 'vue'
import App from './App.vue'
import router from './router'

Vue.config.productionTip = false
import Vuetify from 'vuetify'
import VueCytoscape from 'vue-cytoscape'
import 'vuetify/dist/vuetify.min.css'
import '@mdi/font/css/materialdesignicons.css'

Vue.use(Vuetify)
Vue.use(VueCytoscape)

new Vue({
  icons: {
    iconfont: 'mdi',
  },
  vuetify: new Vuetify({ }),
  router,
  render: h => h(App),
  created: function () {
    window.addEventListener('beforeunload', function () {
      this.alert("fdfgjk")
    }, false)
  },
}).$mount('#app')
