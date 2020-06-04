import Vue from 'vue'
import VueRouter from 'vue-router'
import Ontology from '../views/Ontology.vue'
import Classes from '../views/Classes.vue'
import ObjectProperties from '../views/ObjectProperties.vue'
import DataProperties from '../views/DataProperties.vue'
import Individuals from '../views/Individuals.vue'
import AnalyseOntology from '../views/AnalyseOntology.vue'
import Graph from '../views/Graph.vue'
import Info from '../views/Info.vue'

Vue.use(VueRouter)

  const routes = [
  {
    path: '/',
    name: 'Ontology',
    component: Ontology
  },
  {
    path: '/classes',
    name: 'Classes',
    component: Classes
  },
  {
    path: '/objectProperties',
    name: 'ObjectProperties',
    component: ObjectProperties
  },
  {
    path: '/dataProperties',
    name: 'DataProperties',
    component: DataProperties
  },
  {
    path: '/individuals',
    name: 'Individuals',
    component: Individuals
  },
  {
    path: '/analyseOntology',
    name: 'AnalyseOntology',
    component: AnalyseOntology
  },
  {
    path: '/graph',
    name: 'Graph',
    component: Graph
  },
  {
    path: '/info',
    name: 'Info',
    component: Info
  },
  //{
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    //component: () => import(/* webpackChunkName: "about" */ '../views/Classes.vue')
  //}
]

const router = new VueRouter({
  routes
})

export default router
