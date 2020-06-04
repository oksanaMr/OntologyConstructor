<template>
  <div class="analyseOntology">
    <v-container >
      <h2 class="display-1 ml-5">Анализ онтологии</h2>
      <v-card raised class="mt-2">

          <v-card-title>Выберите продукт и проанализируйте цепочку операций для его изготовления</v-card-title>

        <v-row>
          <v-col cols=2>
            <v-subheader class="ml-5">Product IRI</v-subheader>
          </v-col>
          <v-col cols=5>
            <v-select :items="items" label="Product" outlined v-model="productIRI"></v-select> 
          </v-col>
          <v-col cols="2">
              <v-btn color="primary" v-on:click="analyseOntology" :loading="loading">Анализировать</v-btn>
          </v-col>
        </v-row>

        <v-row>
          <v-col cols=6>
            <v-subheader>Последовательность операций</v-subheader>
            <v-textarea class="ml-5" label="Последовательность операций" v-model="seqOperation" rows="13" solo readonly></v-textarea>
          </v-col>
          <v-col cols=6>
            <v-subheader>Результат анализа</v-subheader>
            <v-textarea class="mr-5" label="Результат анализа" v-model="resultAnalyse" rows="13" solo readonly></v-textarea>
          </v-col>
        </v-row>

      </v-card>
    </v-container>
  </div>
</template>

<script>

import axios from 'axios'

export default {
  name: 'AnalyseOntology',
  data() {
      return{
          productIRI: null,
          seqOperation: "",
          resultAnalyse: "",
          items:[],
          loading: false,
      }
  },

  created(){
      this.getProducts();
  },

  methods: {

    getProducts(){
        axios
        .get("http://localhost:8090/analyseOntology/getProducts")
        .then(response => response.data)
        .then(data => (
            this.items = data
        ));
    },

    analyseOntology(){
      if(this.productIRI != null){
      this.loading = true;
        let file = new Blob([this.productIRI]);
        let formData = new FormData();
        formData.append('file', file);
        axios
        .post("http://localhost:8090/analyseOntology/getAnalyse", formData, {headers: {'Content-Type': 'multipart/form-data'}})
        .then(response => response.data)
        .then(data => {
            this.seqOperation = "";
            this.resultAnalyse = "";
            Object.keys(data.seqOperation).map(operation => {
                this.seqOperation = this.seqOperation + data.seqOperation[operation]
            });

            Object.keys(data.resultAnalyse).map(operation => {
                this.resultAnalyse = this.resultAnalyse + data.resultAnalyse[operation]
            });

            this.loading = false
        })
        .catch(error => {
          this.loading = false;
          this.resultAnalyse = "Анализ не выполнен! Произошла ошибка"
          console.log(error.response)
        })
      }
    },

  }
}
</script>