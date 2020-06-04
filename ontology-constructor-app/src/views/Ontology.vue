<template>
  <div class="ontology">
    <v-container >
      <h2 class="display-1 ml-5">Онтология</h2>
      <v-row>

        <v-col cols="8">
          <v-card raised>
            <v-card-title>Информация</v-card-title>

            <v-container>

              <v-row no-gutters>
                <v-col cols=3>
                  <v-subheader>Ontology IRI</v-subheader>
                </v-col>
                <v-col cols=9>
                  <v-text-field label="Например http://www.example.com/ontologies/myontology/" outlined v-model="ontologyIRI"></v-text-field> 
                </v-col>
              </v-row>

              <v-row no-gutters>
                <v-col cols=3 >
                  <v-subheader>Ontology Version IRI</v-subheader>
                </v-col>
                <v-col cols=9>
                  <v-text-field label="Например http://www.example.com/ontologies/myontology/1.0.0" outlined v-model="ontologyVersionIRI"></v-text-field>
                </v-col>
              </v-row>

            </v-container>

            <v-card-actions>
                <v-btn color="primary" v-on:click="setOntologyIRI">Изменить</v-btn>
              </v-card-actions>
          </v-card>
        </v-col>

        <v-col cols="4">
          <v-card raised>
            <v-card-title>Метрика</v-card-title>
            <v-simple-table>
                <tbody>
                  <tr v-for="item in metrics" :key="item.name">
                    <td>{{ item.name }}</td>
                    <td>{{ item.count }}</td>
                  </tr>
                </tbody>
            </v-simple-table>
          </v-card>
        </v-col>

      </v-row>

    <v-card  raised>
      <v-card-title>Действия</v-card-title>

      <v-card-actions>
          <v-btn color="primary" v-on:click="openOntology">Открыть</v-btn>
          <v-btn color="primary" v-on:click="saveOntology">Сохранить</v-btn>
          <v-btn color="primary" v-on:click="saveOntologyAs">Сохранить как</v-btn>
      </v-card-actions>
    </v-card >

      <v-card class="mt-3" raised>
        <v-card-title>Префиксы</v-card-title>
        <v-data-table 
          :items-per-page="5"
          :headers="headers"
          :items="prefixArray">
        </v-data-table>
      </v-card>

    </v-container>
  </div>
</template>

<script>

import axios from 'axios'

export default {
  name: 'Ontology',
  data() {
      return{
          ontologyIRI: " ",
          ontologyVersionIRI: " ",
          prefixArray: [],
          headers: [
            {text:"Префикс", value: 'prefix'},
            {text:"Значение", value: 'iri'}
          ],
          metrics:[]
      }
  },

  created(){
      this.getOntologyIRI();
      this.getOntologyPrefix();
      this.getMetrics();
  },

  methods: {

    getOntologyIRI(){
      axios
      .get("http://localhost:8090/ontology/getOntologyIRI")
      .then(response => response.data)
      .then(data => (
        this.ontologyIRI = data.OntologyIRI,
        this.ontologyVersionIRI = data.OntologyVersionIRI
      ));
    },

    setOntologyIRI(){
      let file = new Blob([this.ontologyIRI,";",this.ontologyVersionIRI]);
      let formData = new FormData();
      formData.append('file', file);
      axios
      .post("http://localhost:8090/ontology/setOntologyIRI",formData, {headers: {'Content-Type': 'multipart/form-data'}})
      .then(response => response.data)
      .then(data => (
        this.ontologyIRI = data.OntologyIRI,
        this.ontologyVersionIRI = data.OntologyVersionIRI
      ));
    },

    getOntologyPrefix(){
      this.prefixArray = [];
      axios
      .get("http://localhost:8090/ontology/getOntologyPrefix")
      .then(response => response.data)
      .then(data => Object.keys(data).map(pref => {
        let item = {
          prefix: pref,
          iri: data[pref]
        };
        this.prefixArray.push(item)
      }) 
      )
    },

    getMetrics(){
      this.metrics = [];
      axios
      .get("http://localhost:8090/ontology/getMetrics")
      .then(response => response.data)
      .then(data => Object.keys(data).map(metric => {
        let item = {
          name: metric,
          count: data[metric]
        };
        this.metrics.push(item)
      }) 
      )
    },

    openOntology(){
        axios
        .get("http://localhost:8090/ontology/openOntology")
        .then(response => response.data)
        .then(data => {
          if(data == true){
            this.getOntologyIRI();
            this.getOntologyPrefix();
            this.getMetrics();
        }})
    },

    saveOntologyAs(){
        axios
        .get("http://localhost:8090/ontology/saveOntologyAs")
        .then(response => response.data)
    },

    saveOntology(){
        axios
        .get("http://localhost:8090/ontology/saveOntology")
        .then(response => response.data)

    },

  }
}
</script>