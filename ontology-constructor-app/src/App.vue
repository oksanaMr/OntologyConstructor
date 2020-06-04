<template>
  <div id="app">
    <v-app id="inspire">
      <v-navigation-drawer app
        permanent
        src="https://cdn.vuetifyjs.com/images/backgrounds/bg-2.jpg"
        left
        dark
      >
        <v-list>
          <v-list-item>
            <v-list-item-content>
              <v-list-item-title class="title">Конструктор онтологий</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>

        <v-divider></v-divider>

        <v-list nav shaped>

          <v-list-item link to="/">
            <v-list-item-icon>
              <v-icon large>mdi-clipboard-file</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>Онтология</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item link to="/classes">
            <v-list-item-icon>
              <v-icon large>mdi-alpha-c-circle</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Классы</v-list-item-title>
          </v-list-item>

          <v-list-item link to="/objectProperties">
            <v-list-item-icon>
              <v-icon large>mdi-alpha-p-circle</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Объектные свойства</v-list-item-title>
          </v-list-item>

          <v-list-item link to="/dataProperties">
            <v-list-item-icon>
              <v-icon large>mdi-alpha-p-circle</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Свойства данных</v-list-item-title>
          </v-list-item>

          <v-list-item link to="/individuals">
            <v-list-item-icon>
              <v-icon large>mdi-alpha-i-circle</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Объекты</v-list-item-title>
          </v-list-item>

          <v-list-item link to="/graph">
            <v-list-item-icon>
              <v-icon large>mdi-graph</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Граф</v-list-item-title>
          </v-list-item>

          <v-list-item link to="/analyseOntology">
            <v-list-item-icon>
              <v-icon large>mdi-feature-search</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Анализ</v-list-item-title>
          </v-list-item>

          <v-list-item link to="/info">
            <v-list-item-icon>
              <v-icon large>mdi-help-circle</v-icon>
            </v-list-item-icon>
            <v-list-item-title>Справка</v-list-item-title>
          </v-list-item>

        </v-list>
      </v-navigation-drawer>

      <v-content>
        <router-view/>
      </v-content>

      <!--<v-footer app></v-footer>-->

    </v-app>
  </div>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: left;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}


</style>
